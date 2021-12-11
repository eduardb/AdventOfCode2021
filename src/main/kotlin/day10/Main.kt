package day10

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day10/input.txt").readLines()
    val corruptedScores = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137,
    )

    val partOne = input.mapNotNull { line ->
        corrupted(line)
    }.sumOf {
        corruptedScores[it]!!
    }

    println(partOne)

    val incomplete = input.mapNotNull { if (corrupted(it) == null) it else null }

    val incompleteScores = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4,
    )
    val partTwo = incomplete.map { complete(it) }
        .map {
            it.fold(0L) { acc, c -> acc * 5 + incompleteScores[c]!! }
        }
        .sorted()
        .middleValue()

    println(partTwo)
}

private fun <E> List<E>.middleValue(): E {
    return this[size / 2]
}

fun corrupted(line: String): Char? {
    val stack = mutableListOf<Char>()
    line.forEach {
        when (it) {
            '(' -> stack.add(it)
            ')' -> if (stack.lastOrNull() == '(') stack.removeLast() else return it
            '[' -> stack.add(it)
            ']' -> if (stack.lastOrNull() == '[') stack.removeLast() else return it
            '{' -> stack.add(it)
            '}' -> if (stack.lastOrNull() == '{') stack.removeLast() else return it
            '<' -> stack.add(it)
            '>' -> if (stack.lastOrNull() == '<') stack.removeLast() else return it
        }
    }
    return null
}

fun complete(line: String): String {
    val stack = mutableListOf<Char>()
    line.forEach {
        when (it) {
            '(' -> stack.add(it)
            ')' -> if (stack.lastOrNull() == '(') stack.removeLast() else throw IllegalArgumentException("$line is corrupted")
            '[' -> stack.add(it)
            ']' -> if (stack.lastOrNull() == '[') stack.removeLast() else throw IllegalArgumentException("$line is corrupted")
            '{' -> stack.add(it)
            '}' -> if (stack.lastOrNull() == '{') stack.removeLast() else throw IllegalArgumentException("$line is corrupted")
            '<' -> stack.add(it)
            '>' -> if (stack.lastOrNull() == '<') stack.removeLast() else throw IllegalArgumentException("$line is corrupted")
        }
    }

    return stack.reversed().map {
        when (it) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> throw IllegalStateException("$it not recognized")
        }
    }.joinToString(separator = "")
}