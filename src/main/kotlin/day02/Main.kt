package day02

import java.io.File

fun main() {
    val commands = File("src/main/kotlin/day2/input.txt").readLines().map { Command.parseCommand(it) }

    val submarinePartOne = Submarine()
    commands.forEach { it.visitPartOne(submarinePartOne) }
    println(submarinePartOne.run { depth * position })

    val submarinePartTwo = Submarine()
    commands.forEach { it.visitPartTwo(submarinePartTwo) }
    println(submarinePartTwo.run { depth * position })

}

class Submarine {
    var depth = 0
    var position = 0
    var aim = 0
}

sealed class Command(open val value: Int) {

    companion object {
        fun parseCommand(input: String): Command {
            val (rawCommand, rawValue) = input.split(" ")
            val value = rawValue.toInt()
            return when (rawCommand) {
                Forward::class.simpleName!!.lowercase() -> Forward(value)
                Down::class.simpleName!!.lowercase() -> Down(value)
                Up::class.simpleName!!.lowercase() -> Up(value)
                else -> throw IllegalArgumentException("$rawCommand is not a valid command")
            }
        }
    }

    abstract fun visitPartOne(submarine: Submarine)
    abstract fun visitPartTwo(submarine: Submarine)

    class Forward(value: Int) : Command(value) {
        override fun visitPartOne(submarine: Submarine) {
            submarine.position += value
        }

        override fun visitPartTwo(submarine: Submarine) {
            submarine.position += value
            submarine.depth += submarine.aim * value
        }
    }

    class Down(value: Int) : Command(value) {
        override fun visitPartOne(submarine: Submarine) {
            submarine.depth += value
        }

        override fun visitPartTwo(submarine: Submarine) {
            submarine.aim += value
        }

    }

    class Up(value: Int) : Command(value) {
        override fun visitPartOne(submarine: Submarine) {
            submarine.depth -= value
        }

        override fun visitPartTwo(submarine: Submarine) {
            submarine.aim -= value
        }
    }
}
