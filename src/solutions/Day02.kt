package solutions

import println
import readInput

fun main() {
    fun String.splitInHalfOrNull(): List<String>? =
        takeIf { this.isNotEmpty() && this.length % 2 == 0 }
            ?.chunked(this.length / 2)

    fun String.splitByXOrNull(divisor: Int): List<String>? =
        takeIf { this.isNotEmpty() && this.length % divisor == 0 }
            ?.chunked(divisor)

    fun String.toLongRange(): LongRange {
        val (start, end) = this.split("-").map { it.trim().toLong() }
        return start..end
    }

    fun getInputList(input: List<String>): List<String> {
        val inputAsString = input.firstOrNull() ?: throw IllegalArgumentException("Invalid input $input")
        return inputAsString.split(",")
            .filter { it.isNotEmpty() }
            .map { it.trim() }
    }

    fun hasMatchingHalves(n: Long): Boolean =
        n.toString().splitInHalfOrNull()?.let { halves ->
            halves.first() == halves.last()
        } ?: false

    fun hasRepeatingChunks(n: Long): Boolean {
        val s = n.toString()
        val maxChunkSize = s.length / 2

        return (1..maxChunkSize).any { divisor ->
            s.splitByXOrNull(divisor)?.let { chunks ->
                chunks.all { it == chunks.first() }
            } ?: false
        }
    }

    fun part2(input: List<String>): Long {
        return getInputList(input).flatMap { it.toLongRange() }
            .filter(::hasRepeatingChunks)
            .sum()
    }

    fun part1(input: List<String>): Long {
        return getInputList(input).flatMap { it.toLongRange() }
            .filter(::hasMatchingHalves)
            .sum()
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
