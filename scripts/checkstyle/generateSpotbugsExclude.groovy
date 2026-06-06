#!/usr/bin/env groovy
// Скрипт: generateSpotbugsExclude.groovy
// Назначение: Конвертирует один или несколько spotbugs.xml в exclude-фильтр для SpotBugs
// Использование:
//   groovy generateSpotbugsExclude.groovy [inputFile1] [inputFile2] ... [outputFile]
//   Если передан только один аргумент, он считается входным файлом, выходной - по умолчанию.
//   Если аргументов нет, используются значения по умолчанию.

import groovy.xml.MarkupBuilder

// 1. Обработка аргументов командной строки
def inputFiles = []
def outputFile = 'spotbugs-exclude.xml'

if (args.length == 0) {
    inputFiles = ['target/spotbugs.xml']
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
    println 'Использование: groovy generateSpotbugsExclude.groovy [inputFile1] [inputFile2] ... [outputFile]'
    println '  inputFileN - входные XML файлы SpotBugs (по умолчанию: target/spotbugs.xml)'
    println '  outputFile - выходной фильтр исключений (по умолчанию: spotbugs-exclude.xml)'
    System.exit(0)
}

println "Входные файлы: ${inputFiles}"
println "Выходной файл: $outputFile"

// Структура для группировки нарушений: [className -> [bugPattern -> [instances]]]
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

    // Обработка данных из SpotBugs XML
    inputXml.BugInstance.each { bugInstance ->
        def className = bugInstance.Class.@classname.text()
        def bugPattern = bugInstance.@type.text()
        def bugCode = bugInstance.@abbrev.text() ?: bugPattern

        def methodName = bugInstance.Method?.@name?.text() ?: ''
        def methodSignature = bugInstance.Method?.@signature?.text() ?: ''
        def fieldName = bugInstance.Field?.@name?.text() ?: ''

        def instanceInfo = [
                className      : className,
                bugPattern     : bugPattern,
                bugCode        : bugCode,
                methodName     : methodName,
                methodSignature: methodSignature,
                fieldName      : fieldName,
                priority       : bugInstance.@priority?.text() ?: ''
        ]

        // Инициализируем структуру данных
        if (!violations[className]) {
            violations[className] = [:]
        }
        if (!violations[className][bugPattern]) {
            violations[className][bugPattern] = []
        }
        violations[className][bugPattern] << instanceInfo
    }
}

// 3. Проверяем, найдены ли нарушения
if (violations.isEmpty()) {
    println "Не найдено нарушений SpotBugs ни в одном из входных файлов."
    System.exit(0)
}

// 4. Генерация XML-фильтра исключений
def writer = new StringWriter()
def xml = new MarkupBuilder(writer)

xml.doubleQuotes = true

writer.write('<?xml version="1.0" encoding="UTF-8"?>\n')

// Корневой элемент FindBugsFilter
xml.FindBugsFilter {
    // Опциональные глобальные исключения (оставляем как в оригинале)
    Match {
        Bug(pattern: "NM_CONFUSING")
    }
    Match {
        Bug(pattern: "~.*")
        Package(name: "~generated([.].*)?")
    }

    violations.each { className, bugPatterns ->
        bugPatterns.each { bugPattern, instances ->
            Match {
                Class(name: className)
                Bug(pattern: bugPattern)
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
def totalClasses = violations.size()
def totalBugPatterns = violations.values().sum { it.size() }
def totalInstances = violations.values().sum { bugPatterns ->
    bugPatterns.values().sum { it.size() }
}

println "Готово! Статистика:"
println "  Обработано уникальных классов: $totalClasses"
println "  Уникальных типов ошибок: $totalBugPatterns"
println "  Всего экземпляров ошибок: $totalInstances"
println "  Файл сохранён как: $outputFile"