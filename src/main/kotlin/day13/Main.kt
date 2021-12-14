package day13

import java.io.File

const val DEBUG = false

fun main() {
    val input = File("src/main/kotlin/day13/input.txt").readLines()

    var maxX = 0
    var maxY = 0

    val dots = input.asSequence()
        .takeWhile { it != "" }
        .map { it.split(",") }
        .map { (x, y) ->
            val xInt = x.toInt()
            val yInt = y.toInt()
            if (xInt > maxX) maxX = xInt
            if (yInt > maxY) maxY = yInt

            xInt to yInt
        }
        .toList()

    val paper = Array(maxY + 1) { CharArray(maxX + 1) { '.' } }

    dots.forEach { (x, y) ->
        paper[y][x] = '#'
    }

    if (DEBUG) {
        paper.forEach(::println); println()
    }

    val folds = input.takeLast(input.size - dots.size - 1)
        .map {
            val matches = "fold along ([x|y])=(\\d+)".toRegex().matchEntire(it)
            val (_, dir, i) = matches!!.groupValues
            dir to i.toInt()
        }

    var folded = when (folds.first().first) {
        "x" -> foldHorizontal(paper, folds.first().second)
        "y" -> foldVertical(paper, folds.first().second)
        else -> throw IllegalArgumentException()
    }

    val partOne = folded.sumOf { line -> line.count { it == '#' } }
    println(partOne)

    if (DEBUG) {
        folded.forEach(::println); println()
    }

    folds.drop(1).forEach { fold ->
        folded = when (fold.first) {
            "x" -> foldHorizontal(folded, fold.second)
            "y" -> foldVertical(folded, fold.second)
            else -> throw IllegalArgumentException()
        }
        if (DEBUG) {
            folded.forEach(::println); println()
        }
    }

    folded.forEach(::println)

}


/**
 * Fold left along vertical X line
 */
fun foldHorizontal(paper: Array<CharArray>, xLine: Int): Array<CharArray> {
    val folded = Array(paper.size) { y -> CharArray(xLine) { x -> paper[y][x] } }

    paper.indices.forEach { y ->
        (xLine until paper[y].size).forEach { x ->
            if (paper[y][x] == '#') {
                folded[y][2 * xLine - x] = '#'
            }
        }

    }

    return folded
}

/**
 * Fold up along horizontal Y line
 */
fun foldVertical(paper: Array<CharArray>, yLine: Int): Array<CharArray> {
    val folded = paper.take(yLine)
    (yLine until paper.size).forEach { line ->
        paper[line].forEachIndexed { x, c ->
            if (c == '#') {
                folded[2 * yLine - line][x] = '#'
            }
        }
    }
    return folded.toTypedArray()
}
