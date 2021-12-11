package day08

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day8/input.txt")
        .readLines()
        .map {
            val (patterns, output) = it.split(" | ")
            patterns.split(" ") to output.split(" ")
        }

    val partOne = input.sumOf { (_, output) ->
        output.count { it.length in listOf(2, 3, 4, 7) }
    }
    println(partOne)

    val digits = mapOf(
         0 to "abcefg",
         1 to "cf",
         2 to "acdeg",
         3 to "acdfg",
         4 to "bcdf",
         5 to "abdfg",
         6 to "abdefg",
         7 to "acf",
         8 to "abcdefg",
         9 to "abcdfg",
    )

    val partTwo = input.sumOf { (patterns, output) ->
        val digit1 = patterns.single { it.length == digits[1]!!.length }
        val digit4 = patterns.single { it.length == digits[4]!!.length }
        val digit7 = patterns.single { it.length == digits[7]!!.length }
        val digit8 = patterns.single { it.length == digits[8]!!.length }

        // find "a" signal as difference between patterns for digit 7 and digit 1
        val signalA = (digit7.toSet() - digit1.toSet()).single()

        // find digit "9" as the one with one signal ("g") missing after adding the signals for digit 4 and signal "a"
        val digit9 = patterns.single { it.length == digits[9]!!.length && (it.toSet() - (digit4.toSet() + signalA)).size == 1 }
        val signalG = (digit9.toSet() - (digit4.toSet() + signalA)).single()

        val signalE = (digit8.toSet() - digit4.toSet() - signalA - signalG).single()

        val digit3 = patterns.single { it.length == digits[3]!!.length && it.containsAll(digit1.toSet() + signalA + signalG) }
        val signalD = (digit3.toSet() - digit1.toSet() - signalA - signalG).single()

        val digit2 = patterns.single { it.length == digits[2]!!.length && it.containsAll(setOf(signalA, signalD, signalE, signalG)) }
        val signalC = (digit2.toSet() - signalA - signalD - signalE - signalG).single()

        val signalF = (digit1.toSet() - signalC).single()
        val signalB = (digit4.toSet() - digit1.toSet() - signalD).single()

        output.map { digit ->
            digit.map {
                when (it) {
                    signalA -> 'a'
                    signalB -> 'b'
                    signalC -> 'c'
                    signalD -> 'd'
                    signalE -> 'e'
                    signalF -> 'f'
                    signalG -> 'g'
                    else -> throw IllegalArgumentException("Unknown signal $it")
                }
            }.sorted().joinToString(separator = "").let { sortedDigit -> digits.filterValues { it == sortedDigit }.keys.single() }
        }.joinToString(separator = "").toInt()
    }

    println(partTwo)

}

fun String.containsAll(chars: Set<Char>) = chars.all { this.contains(it) }