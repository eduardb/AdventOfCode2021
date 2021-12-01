package day1

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day1/input.txt").readLines().map { it.toInt() }

    val partOne = input.windowed(2)
        .count { (first, second) -> first < second }
    println(partOne)

    val partTwo = input.windowed(3)
        .map { window -> window.sum() }
        .windowed(2)
        .count { (first, second) -> first < second }
    println(partTwo)
}