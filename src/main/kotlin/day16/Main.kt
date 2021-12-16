package day16

import java.io.File

fun main() {
    File("src/main/kotlin/day16/input.txt").readLines().forEach { line ->
        val binary = line.map { it.toString().toInt(16).toString(2).padStart(4, '0') }.joinToString(separator = "")

        println(line)

        val packet = Packet.parse(binary)

        println("Sum of all versions = ${packet.versionSum()}")
        println(packet)
        println("Value = ${packet.value}")
    }
}


sealed class Packet(val version: Int, val lengthInBits: Int) {

    abstract val value: Long

    companion object {
        fun parse(input: String): Packet {
            val version = input.take(3).toInt(2)

            return when (val typeId = input.substring(3, 6).toInt(2)) {
                LiteralValue.TYPE_ID -> LiteralValue.parse(input.substring(6), version)
                else -> Operator.parse(input.substring(6), typeId, version)
            }
        }
    }

    abstract fun versionSum(): Int

    class LiteralValue private constructor(override val value: Long, version: Int, lengthInBits: Int) :
        Packet(version, lengthInBits) {

        companion object {
            const val TYPE_ID = 4

            fun parse(input: String, version: Int): Packet {
                val chunks = input.windowed(5, 5)
                val lastChunk = chunks.indexOfFirst { it.first() == '0' }
                val value = chunks.subList(0, lastChunk + 1).joinToString(separator = "") { it.takeLast(4) }

                return LiteralValue(value.toLong(2), version, (lastChunk + 1) * 5 + 6)
            }
        }

        override fun versionSum(): Int {
            return version
        }

        override fun toString(): String {
            return value.toString()
        }

    }

    class Operator private constructor(val typeId: Int, val subpackets: List<Packet>, version: Int, lengthInBits: Int) :
        Packet(version, lengthInBits) {

        companion object {
            private const val TOTAL_LENGTH_IN_BITS_LENGTH_TYPE_ID = '0'
            private const val NUMBER_OF_SUBPACKETS_LENGTH_TYPE_ID = '1'

            fun parse(input: String, typeId: Int, version: Int): Operator {
                when (input.first()) {
                    TOTAL_LENGTH_IN_BITS_LENGTH_TYPE_ID -> {
                        val totalLength = input.substring(1, 16).toInt(2)
                        var length = 0
                        val subpackets = mutableListOf<Packet>()
                        while (length < totalLength) {
                            subpackets.add(parse(input.substring(16 + length)))
                            length += subpackets.last().lengthInBits
                        }
                        return Operator(typeId, subpackets, version, totalLength + 6 + 16)
                    }
                    NUMBER_OF_SUBPACKETS_LENGTH_TYPE_ID -> {
                        val subpacketsCount = input.substring(1, 12).toInt(2)
                        var length = 0
                        val subpackets = mutableListOf<Packet>()
                        repeat(subpacketsCount) {
                            subpackets.add(parse(input.substring(12 + length)))
                            length += subpackets.last().lengthInBits
                        }
                        return Operator(typeId, subpackets, version, length + 6 + 12)
                    }
                    else -> throw IllegalArgumentException("Unknown length type id ${input.first()}")
                }
            }
        }

        override val value: Long
            get() = when (typeId) {
                0 -> subpackets.sumOf { it.value }
                1 -> subpackets.fold(1L) { acc, cur -> acc * cur.value }
                2 -> subpackets.minOfOrNull { it.value } ?: 0
                3 -> subpackets.maxOfOrNull { it.value } ?: 0
                5 -> if (subpackets[0].value > subpackets[1].value) 1 else 0
                6 -> if (subpackets[0].value < subpackets[1].value) 1 else 0
                7 -> if (subpackets[0].value == subpackets[1].value) 1 else 0
                else -> throw IllegalArgumentException("Unknown operator type $typeId")
            }

        override fun versionSum(): Int {
            return version + subpackets.sumOf { it.versionSum() }
        }

        override fun toString(): String {
            val op = when (typeId) {
                0 -> "+"
                1 -> "*"
                2 -> "min"
                3 -> "max"
                5 -> ">"
                6 -> "<"
                7 -> "="
                else -> throw IllegalArgumentException("Unknown operator type $typeId")
            }
            return "(${subpackets.joinToString(" $op ")})"
        }


    }

}