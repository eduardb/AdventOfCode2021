package day09

import java.io.File

enum class Direction(val rowOffset: Int, val columnOffset: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1)
}

fun main() {
    val heightmap = File("src/main/kotlin/day9/input.txt").readLines()
        .map { line -> line.map { it.toString().toUShort() }.toMutableList() }
    val rows = heightmap.size
    val columns = heightmap.first().size

    val lowPoints = mutableListOf<Pair<Int, Int>>()

    fun isLowestPoint(row: Int, column: Int): Boolean {
        val height = heightmap[row][column]
        return Direction.values().all {
            val newRow = row + it.rowOffset
            val newColumn = column + it.columnOffset
            newRow < 0 || newRow >= rows ||
                    newColumn < 0 || newColumn >= columns ||
                    heightmap[newRow][newColumn] > height
        }
    }

    (0 until rows).forEach { row ->
        (0 until columns).forEach { column ->
            if (isLowestPoint(row, column)) lowPoints.add(row to column)
        }
    }

    val partOne = lowPoints.map { heightmap[it.first][it.second] }.sum() + lowPoints.size.toUInt()
    println(partOne)

    fun basinSize(startRow: Int, startColumn: Int): Int {
        var size = 0
        val queue = mutableListOf(startRow to startColumn)
        heightmap[startRow][startColumn] = UShort.MAX_VALUE

        while (queue.isNotEmpty()) {
            size++
            val (row, column) = queue.removeFirst()

            Direction.values().forEach {
                val newRow = row + it.rowOffset
                val newColumn = column + it.columnOffset
                if (newRow < 0 || newRow >= rows ||
                    newColumn < 0 || newColumn >= columns ||
                    heightmap[newRow][newColumn] == 9.toUShort() || heightmap[newRow][newColumn] == UShort.MAX_VALUE
                ) return@forEach
                queue.add(newRow to newColumn)
                heightmap[newRow][newColumn] = UShort.MAX_VALUE
            }
        }
        return size
    }

    val partTwo = lowPoints.map { (row, column) ->
        basinSize(row, column)
    }.sortedDescending().subList(0, 3).reduce { acc, s -> acc * s }

    println(partTwo)
}

