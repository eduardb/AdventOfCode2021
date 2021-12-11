package day11

import java.io.File

val adjacentRowsOffsets   = arrayOf(-1, -1, -1,  0, 0,  1, 1, 1)
val adjacentColumnOffsets = arrayOf(-1,  0,  1, -1, 1, -1, 0, 1)

const val size = 10

fun main() {
    val input = File("src/main/kotlin/day11/input.txt").readLines()
        .map { line -> line.map { it.toString().toUShort() }.toMutableList() }

    var flashes = 0
    repeat(Int.MAX_VALUE) { step ->
        val flashed = mutableListOf<Pair<Int, Int>>()
        (0 until size).forEach { row ->
            (0 until size).forEach column@{ column ->
                if (row to column in flashed) return@column
                input[row][column]++

                val flashing = mutableListOf<Pair<Int, Int>>()

                if (input[row][column] == 10.toUShort()) {
                    input[row][column] = 0u
                    flashing += row to column
                }
                while (flashing.isNotEmpty()) {
                    val (r, c) = flashing.removeFirst()
                    flashed += r to c
                    (0..7).forEach adjacent@{ adjacentIndex ->
                        val newRow = r + adjacentRowsOffsets[adjacentIndex]
                        val newColumn = c + adjacentColumnOffsets[adjacentIndex]
                        if (newRow < 0 || newRow >= size ||
                            newColumn < 0 || newColumn >= size ||
                            newRow to newColumn in flashed ||
                            newRow to newColumn in flashing) return@adjacent
                        input[newRow][newColumn]++
                        if (input[newRow][newColumn] == 10.toUShort()) {
                            input[newRow][newColumn] = 0u
                            flashing += newRow to newColumn
                        }
                    }
                }
            }
        }

        flashes += flashed.size

        if (step == 99) {
            println(flashes)
        }

        if (flashed.size == 100) {
            println(step + 1)
            return
        }

//        debug(step + 1, input)
    }
}

fun debug(step: Int, input: List<MutableList<UShort>>) {
    println("After step $step:")
    input.forEach {
        println(it.joinToString(separator = ""))
    }
    println()
}