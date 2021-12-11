package day05

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day5/input.txt").readLines()
        .map { line ->
            val (start, end) = line.split(" -> ")
            Segment(
                start.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) },
                end.split(",").let { (x, y) -> Point(x.toInt(), y.toInt()) },
            )
        }

    val diagram = mutableMapOf<Point, Int>()

    input
        .filter { it.isHorizontal() || it.isVertical() }
        .forEach { s ->
            s.points().forEach { p ->
                diagram[p] = diagram.getOrDefault(p, 0) + 1
            }
        }
    println(diagram.count { entry -> entry.value > 1 })

    diagram.clear()

    input.forEach { s ->
        s.points().forEach { p ->
            diagram[p] = diagram.getOrDefault(p, 0) + 1
        }
    }
    println(diagram.count { entry -> entry.value > 1 })
}

data class Point(val x: Int, val y: Int)

data class Segment(val start: Point, val end: Point) {

    fun isHorizontal(): Boolean = start.x == end.x

    fun isVertical(): Boolean = start.y == end.y

    fun points(): List<Point> =
        if (isHorizontal()) {
            (start.y to end.y).map { y -> Point(start.x, y) }
        } else if (isVertical()) {
            (start.x to end.x).map { x -> Point(x, start.y) }
        } else {
            (start to end).asSequence().toList()
        }
}

infix fun Int.to(other: Int): IntRange = if (this < other) this..other else other..this

infix fun Point.to(other: Point): DiagonalLineIterator = DiagonalLineIterator(this, other)

class DiagonalLineIterator(start: Point, private val end: Point): Iterator<Point> {

    private var next = start
    private var hasNext = true

    private val xIncreasing = start.x <= end.x
    private val yIncreasing = start.y <= end.y

    override fun hasNext() = hasNext

    override fun next(): Point {
        val value = next
        if (value == end) {
            hasNext = false
        } else {
            next = Point(
                if (xIncreasing) next.x + 1 else next.x - 1,
                if (yIncreasing) next.y + 1 else next.y - 1,
            )
        }

        return value
    }
}