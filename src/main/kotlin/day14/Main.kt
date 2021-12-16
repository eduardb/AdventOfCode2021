@file:OptIn(ExperimentalStdlibApi::class)

package day14

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day14/input.txt").readLines()

    val template = input.first()
    val pairInsertionRules = buildMap<CharSequence, Char> {
        input.asSequence().drop(2).forEach {
            val (pair, insertion) = it.split(" -> ")
            this[pair] = insertion.first()
        }
    }

    println(solveFor(template, pairInsertionRules, 10))
    println(solveFor(template, pairInsertionRules, 40))
}

fun solveFor(template: String, pairInsertionRules: Map<CharSequence, Char>, steps: Int): Long {
    var pairsCount = buildMap<String, Long> {
        template.windowedSequence(2).forEach {
            this[it] = (this[it] ?: 0) + 1
        }
    }

    repeat(steps) { step ->
        pairsCount = buildMap map@{
            pairsCount.forEach { (pair, count) ->
                pairInsertionRules[pair]?.let {
                    val first = "${pair[0]}$it"
                    val second = "$it${pair[1]}"

                    this[first] = (this[first] ?: 0) + count
                    this[second] = (this[second] ?: 0) + count
                } ?: run { this@map[pair] = count }
            }
        }
    }
    val counts = buildMap<Char, Long> {
        pairsCount.forEach { (pair, count) ->
            this[pair[0]] = (this[pair[0]] ?: 0L) + count
            this[pair[1]] = (this[pair[1]] ?: 0L) + count
        }
        this[template.first()] = (this[template.first()] ?: 0L) + 1
        this[template.last()] = (this[template.last()] ?: 0L) + 1
    }

    return ((counts.values.maxOrNull() ?: 0) - (counts.values.minOrNull() ?: 0)) / 2
}