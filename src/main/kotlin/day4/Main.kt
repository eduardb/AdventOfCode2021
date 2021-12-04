package day4

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day4/input.txt").readLines()
    val numbersDrawn = input.first().split(",").map { it.toInt() }

    val boards = input.subList(1, input.size).chunked(6) { rawBoard ->
        rawBoard.subList(1, 6).map { rawRow ->
            rawRow.split(" ")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .toIntArray()
        }.toTypedArray()
    }.map { Board(it) }

    partOne(numbersDrawn, boards)

    partTwo(numbersDrawn, boards)
}

private fun partOne(numbersDrawn: List<Int>, boards: List<Board>) {
    numbersDrawn.forEach { numberDrawn ->
        boards.forEach { board ->
            board.mark(numberDrawn)
            if (board.isWinning()) {
                println(numberDrawn * board.sumOfUnmarkedNumbers())
                return
            }
        }
    }
}

private fun partTwo(numbersDrawn: List<Int>, boards: List<Board>) {
    var remainingBoards = boards
    numbersDrawn.forEach { numberDrawn ->
        boards.forEach { board ->
            board.mark(numberDrawn)
            if (board.isWinning()) {
                remainingBoards = remainingBoards - board
            }
            if (remainingBoards.isEmpty()) {
                println(numberDrawn * board.sumOfUnmarkedNumbers())
                return
            }
        }
    }
}

class Board(private val numbers: Array<IntArray>) {

    fun mark(number: Int) {
        (0 until 5).forEach { row ->
            (0 until 5).forEach{ column ->
                if (numbers[row][column] == number) numbers[row][column] = -1
            }
        }
    }

    fun sumOfUnmarkedNumbers(): Int {
        return numbers.sumOf { row -> row.sumOf { it -> when(it) { -1 -> 0; else -> it} } }
    }

    fun isWinning(): Boolean {
        return numbers.any { row -> row.all { it == -1 } }  ||
                (0 until 5).any { column -> (0 until 5).all { row -> numbers[row][column] == -1  } }
    }

}