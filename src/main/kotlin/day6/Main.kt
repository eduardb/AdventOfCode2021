package day6

import java.io.File

@ExperimentalStdlibApi
fun main() {
    val input = File("src/main/kotlin/day6/input.txt").readText().split(",").map(String::toInt)
    val fishes = input.groupBy { it }.mapValues { it.value.size.toLong() }.toMutableMap()

    var newFishes: Long
    repeat(256) { _ ->
        newFishes = fishes[0] ?: 0
        (0..7).forEach {
            fishes[it] = fishes[it + 1] ?: 0
        }
        fishes[8] = newFishes
        fishes[6] = (fishes[6] ?: 0) + newFishes
    }
    println(fishes.values.sum())
}
