package day3

import java.io.File

fun main() {
    val diagnosticReport = File("src/main/kotlin/day3/input.txt").readLines()

    part1(diagnosticReport)

    part2(diagnosticReport)
}

private fun part1(diagnosticReport: List<String>) {
    val (occurrence1, width, height) = decode(diagnosticReport)

    val gammaString = (0 until width).map { position ->
        if (occurrence1[position] > height / 2) '1' else '0'
    }.joinToString(separator = "")

    val gamma = gammaString.toInt(2)
    val epsilon = "1".repeat(width).toInt(2).xor(gamma)
    println(gamma * epsilon)
}

private fun part2(diagnosticReport: List<String>) {
    val oxygenGeneratorReport = part2RatingReport(diagnosticReport) {foo: Boolean -> if (foo) '1' else '0'}
    val co2ScrubberReport = part2RatingReport(diagnosticReport) {foo: Boolean -> if (!foo) '1' else '0'}
    println(oxygenGeneratorReport * co2ScrubberReport)
}

private fun part2RatingReport(diagnosticReport: List<String>, function: (Boolean) -> Char): Int {
    var ratingReport = diagnosticReport
    var position = 0
    while (ratingReport.size > 1) {
        val (occurrence1, _, height) = decode(ratingReport)
        val mostCommon = function (occurrence1[position] >= height / 2.0)
        ratingReport = ratingReport.filter { it[position] == mostCommon }
        position++
    }
    return ratingReport.first().toInt(2)
}

/**
 * Returns a Triple with the occurrences of 1 for each position, and the width and height of the report
 */
fun decode(diagnosticReport: List<String>): Triple<IntArray, Int, Int> {
    val width = diagnosticReport.first().length
    val height = diagnosticReport.size
    val occurrence1 = IntArray(width)

    (0 until width).forEach { position ->
        (0 until height).forEach { line ->
            if (diagnosticReport[line][position] == '1') occurrence1[position]++
        }
    }

    return Triple(occurrence1, width, height)
}
