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

    /**
     * Tried to make a more kotlin-idiomatic solution here
     * We calculate the max chunk size then loop from 1, to that limit, splitting the string into bigger, even
     * chunks. If all the chunks match, then we have an invalid entry so we add it to the result
     */
    fun hasRepeatingChunks(n: Long): Boolean {
        val s = n.toString()
        val maxChunks = s.length / 2

        return (1..maxChunks).any { divisor ->
            s.splitByXOrNull(divisor)?.let { chunks ->
                chunks.all { it == chunks.first() }
            } ?: false
        }
    }

    fun part2(input: List<String>): Long {
        // all data is in first index of input
        val inputString = input.firstOrNull() ?: throw IllegalArgumentException("Invalid input $input")
        // split input by commas into a new list
        val inputList = inputString.split(",").map { it.trim() }

        // for this part we filter those results without matching 'chunks' rather than 'halves'
        return inputList.flatMap { it.toLongRange() }
            .filter(::hasRepeatingChunks)
            .sum()
    }

    fun hasMatchingHalves(n: Long): Boolean =
        n.toString().splitInHalfOrNull()?.let { it.first() == it.last() } ?: false

    fun part1(input: List<String>): Long {
        // all data is in first index of input
        val inputString = input.firstOrNull() ?: throw IllegalArgumentException("Invalid input $input")
        // split input by commas into a new list
        val inputList = inputString.split(",").map { it.trim() }
        // with the input list we create an iterable of the entries as ranges, filtering out those which do not
        // match the criteria, and summing the result
        return inputList.flatMap { it.toLongRange() }
            .filter(::hasMatchingHalves)
            .sum()
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
