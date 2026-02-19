#!/usr/bin/env groovy
// Скрипт: generateSuppressions.groovy
// Назначение: Конвертирует один или несколько checkstyle-result.xml в checkstyle-suppressions.xml
// Использование:
//   groovy generateSuppressions.groovy [inputFile1] [inputFile2] ... [outputFile]
//   Если передан только один аргумент, он считается входным файлом, выходной - по умолчанию.
//   Если аргументов нет, используются значения по умолчанию.

import groovy.xml.MarkupBuilder

// 1. Обработка аргументов командной строки
def inputFiles = []
def outputFile = 'checkstyle-suppressions.xml'

if (args.length == 0) {
    inputFiles = ['target/checkstyle-result.xml']
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
    println 'Использование: groovy generateSuppressions.groovy [inputFile1] [inputFile2] ... [outputFile]'
    println '  inputFileN - входные XML файлы Checkstyle (по умолчанию: target/checkstyle-result.xml)'
    println '  outputFile - выходной файл исключений (по умолчанию: checkstyle-suppressions.xml)'
    System.exit(0)
}

println "Входные файлы: ${inputFiles}"
println "Выходной файл: $outputFile"

// Структура для группировки нарушений: [fileName -> [checkName -> [lineNumbers]]]
def violations = [:]

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

    inputXml.file.each { fileNode ->
        def fileName = fileNode.@name.text()
        def filePattern = ".*" + new File(fileName).getName()

        fileNode.error.each { error ->
            def source = error.@source.text()
            def line = error.@line.text()

            // Извлекаем короткое имя проверки из полного имени класса
            def checkName
            if (source.contains('.')) {
                def parts = source.split('\\.')
                def lastPart = parts[-1]
                checkName = lastPart.replaceAll('Check$', '')
            } else {
                checkName = source
            }

            // Инициализируем структуру данных
            if (!violations[filePattern]) {
                violations[filePattern] = [:]
            }
            if (!violations[filePattern][checkName]) {
                violations[filePattern][checkName] = [] as Set
            }

            // Добавляем номер строки
            if (line && line.matches('\\d+')) {
                violations[filePattern][checkName] << line.toInteger()
            }
        }
    }
}

// 3. Проверяем, найдены ли нарушения
if (violations.isEmpty()) {
    println "Не найдено нарушений Checkstyle ни в одном из входных файлов."
    System.exit(0)
}

// 4. Генерация XML с подавлениями
def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.doubleQuotes = true

// Добавляем XML-декларацию и комментарии
writer.write('<?xml version="1.0" encoding="UTF-8"?>\n')

// DOCTYPE declaration
writer.write('<!DOCTYPE suppressions PUBLIC\n')
writer.write('    "-//Checkstyle//DTD SuppressionFilter Configuration 1.2//EN"\n')
writer.write('    "https://checkstyle.org/dtds/suppressions_1_2.dtd">\n')

// Корневой элемент suppressions
xml.suppressions {
    violations.each { fileName, checks ->
        checks.each { checkName, lineNumbersSet ->
            def lineNumbersList = lineNumbersSet as List
            if (!lineNumbersList.isEmpty()) {
                lineNumbersList.sort()
                def ranges = convertToRanges(lineNumbersList)

                suppress(
                        files: fileName,
                        checks: checkName,
                        lines: ranges.join(',')
                )
            }
        }
    }
}

// 5. Сохранение результата
try {
    new File(outputFile).withWriter('UTF-8') { writerFile ->
        writerFile.write(writer.toString())
    }
} catch (Exception e) {
    println "Ошибка при записи файла: ${e.message}"
    System.exit(1)
}

// 6. Статистика
def totalFiles = violations.size()
def totalSuppressions = violations.values().sum { it.size() }
def totalLines = violations.values().sum { ruleMap ->
    ruleMap.values().sum { it.size() }
}

println "Готово! Статистика:"
println "  Обработано уникальных файлов: $totalFiles"
println "  Создано правил подавления: $totalSuppressions"
println "  Подавлено нарушений: $totalLines"
println "  Файл сохранён как: $outputFile"

// 7. Вспомогательная функция для преобразования списка строк в диапазоны
def convertToRanges(List lines) {
    if (!lines || lines.isEmpty()) return []

    def ranges = []
    def start = lines[0]
    def prev = start

    for (i in 1..<lines.size()) {
        if (lines[i] > prev + 1) {
            ranges.add(start == prev ? "$start" : "$start-$prev")
            start = lines[i]
        }
        prev = lines[i]
    }

    // Добавляем последний диапазон
    ranges.add(start == prev ? "$start" : "$start-$prev")
    return ranges
}