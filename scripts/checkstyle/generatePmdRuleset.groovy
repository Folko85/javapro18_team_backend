#!/usr/bin/env groovy
// Скрипт: generatePmdRuleset.groovy
// Назначение: Конвертирует один или несколько pmd.xml в PMD ruleset с exclude-pattern
// Использование:
//   groovy generatePmdRuleset.groovy [inputFile1] [inputFile2] ... [outputFile]
//   Если передан только один аргумент, он считается входным файлом, выходной - по умолчанию.
//   Если аргументов нет, используются значения по умолчанию.

import groovy.xml.MarkupBuilder

// 1. Обработка аргументов командной строки
def inputFiles = []
def outputFile = 'pmd-baseline-ruleset.xml'

if (args.length == 0) {
    inputFiles = ['target/pmd.xml']
} else if (args.length == 1) {
    inputFiles = [args[0]]
} else if (args.length == 2) {
    inputFiles = [args[0]]
    outputFile = args[1]
} else {
    // больше двух аргументов: последний - выходной файл, остальные - входные
    outputFile = args[-1]
    inputFiles = args[0..-2] as List
}

if (args.contains('-h') || args.contains('--help')) {
    println 'Использование: groovy generatePmdRuleset.groovy [inputFile1] [inputFile2] ... [outputFile]'
    println '  inputFileN - входные XML файлы PMD (по умолчанию: target/pmd.xml)'
    println '  outputFile - выходной ruleset файл (по умолчанию: pmd-baseline-ruleset.xml)'
    System.exit(0)
}

println "Входные файлы: ${inputFiles}"
println "Выходной файл: $outputFile"

// Множество для хранения уникальных паттернов файлов
def filesWithViolations = [] as Set

// 2. Обработка каждого входного файла
inputFiles.each { inputFile ->
    def inputFileObj = new File(inputFile)
    if (!inputFileObj.exists()) {
        println "Предупреждение: Входной файл '$inputFile' не найден. Пропускаем."
        return
    }

    println "Чтение отчёта из: $inputFile"

    def inputXml
    try {
        inputXml = new XmlSlurper().parse(inputFileObj)
    } catch (Exception e) {
        println "Ошибка при чтении XML из $inputFile: ${e.message}"
        return
    }

    // Собираем уникальные имена файлов с нарушениями
    inputXml.file.each { fileNode ->
        def fileName = fileNode.@name.text()
        // Преобразуем абсолютный путь в паттерн для exclude-pattern
        def pattern = fileName.replaceAll('^.*src/main/java/', '.*/')
        filesWithViolations.add(pattern)
    }
}

// 3. Проверяем, найдены ли нарушения
if (filesWithViolations.isEmpty()) {
    println "Не найдено нарушений PMD ни в одном из входных файлов."
    System.exit(0)
}

// 4. Генерация Ruleset XML
def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.doubleQuotes = true
xml.mkp.xmlDeclaration(version: '1.0', encoding: 'UTF-8')

xml.ruleset(name: "Baseline Suppressions",
        xmlns: "http://pmd.sourceforge.net/ruleset/2.0.0",
        "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
        "xsi:schemaLocation": "http://pmd.sourceforge.net/ruleset/2.0.0 https://pmd.sourceforge.io/ruleset_2_0_0.xsd") {

    description("Исключения для существующих нарушений, сгенерировано: ${new Date()}")

    // Добавляем exclude-pattern для каждого файла
    filesWithViolations.each { pattern ->
        'exclude-pattern'(pattern)
    }

    // Подключаем все нужные категории правил (с исключениями, как в оригинале)
    rule(ref: "category/java/bestpractices.xml")
    rule(ref: "category/java/codestyle.xml") {
        exclude(name: "OnlyOneReturn")
        exclude(name: "LongVariable")
        exclude(name: "ShortVariable")
        exclude(name: "ShortMethodName")
        exclude(name: "ShortClassName")
        exclude(name: "UnnecessaryConstructor")
        exclude(name: "TooManyStaticImports")
    }
    rule(ref: "category/java/design.xml") {
        exclude(name: "ExcessivePublicCount")
        exclude(name: "DataClass")
        exclude(name: "TooManyFields")
        exclude(name: "TooManyMethods")
        exclude(name: "LawOfDemeter")
        exclude(name: "ExcessiveImports")
        exclude(name: "CouplingBetweenObjects")
        exclude(name: "CyclomaticComplexity")
        exclude(name: "CognitiveComplexity")
        exclude(name: "NcssCount")
    }
    rule(ref: "category/java/errorprone.xml")
    rule(ref: "category/java/performance.xml")
}

// 5. Сохранение
try {
    new File(outputFile).withWriter('UTF-8') { it.write(writer.toString()) }
} catch (Exception e) {
    println "Ошибка при записи файла: ${e.message}"
    System.exit(1)
}

// 6. Статистика
println "Готово! Создано ${filesWithViolations.size()} исключений (уникальных файлов/паттернов)."
println "Подключите файл как <ruleset> в конфигурации maven-pmd-plugin"