package day12

import java.io.File

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    val input = File("src/main/kotlin/day12/input.txt").readLines().map { it.split("-") }
    val map = buildMap<String, List<String>> {
        input.forEach { (first, second) ->
            if (second != "start" && first != "end") this[first] = (this[first] ?: emptyList()) + second
            if (first != "start" && second != "end") this[second] = (this[second] ?: emptyList()) + first
        }
    }

    val paths = mutableListOf<List<String>>()
    val currentPath = listOf("start")

    solve(currentPath, map, paths, visitedSmallCaveTwice = true)

    println(paths.size)

    paths.clear()

    solve(currentPath, map, paths, visitedSmallCaveTwice = false)

    println(paths.size)
}

fun solve(
    currentPath: List<String>,
    map: Map<String, List<String>>,
    paths: MutableList<List<String>>,
    visitedSmallCaveTwice: Boolean
) {
    if (currentPath.last() == "end") {
        paths += currentPath
        return
    }
    map[currentPath.last()]?.forEach { next ->
        if (next.isBigCave() || !currentPath.contains(next)) {
            solve(currentPath + next, map, paths, visitedSmallCaveTwice)
        } else {
            if (!visitedSmallCaveTwice) solve(currentPath + next, map, paths, visitedSmallCaveTwice = true)
        }
    }
}

fun String.isBigCave() = this.all { it.isUpperCase() }