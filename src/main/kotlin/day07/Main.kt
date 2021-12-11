package day07

import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/main/kotlin/day7/input.txt").readText().split(",").map(String::toInt)

    val min = input.minOrNull() ?: 0
    val max = input.maxOrNull() ?: 0

    val minFuel1 = (min..max).minOfOrNull { position ->
        input.sumOf { abs(it - position) }
    }

    println(minFuel1)

    val minFuel2 = (min..max).minOfOrNull { position ->
        input.sumOf { sumFrom1ToN(abs(it - position)) }
    }

    println(minFuel2)
}

fun sumFrom1ToN(n: Int) = (1 + n) * n / 2