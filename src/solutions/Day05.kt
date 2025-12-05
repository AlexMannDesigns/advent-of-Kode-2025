package solutions

import println
import readInput

fun main() {
    fun createRange(codeRange: String): Pair<Long, Long> {
        val dashIdx = codeRange.indexOfFirst { it == '-' }
        return Pair(codeRange.take(dashIdx).toLong(), codeRange.substring(dashIdx + 1, codeRange.length).toLong())
    }

    fun rangesOverlap(a: Pair<Long, Long>, b: Pair<Long, Long>): Boolean = a.second >= b.first

    fun mergeOverlappingRanges(ranges: MutableList<Pair<Long, Long>>): MutableList<Pair<Long, Long>> {
        for (i in 0 until ranges.size - 1) {
            val a = ranges[i]
            val b = ranges[i + 1]
            if (rangesOverlap(a, b)) {
                val newEntry = Pair(minOf(a.first, b.first), maxOf(a.second, b.second))
                ranges.removeAt(i)
                ranges.removeAt(i)
                ranges.add(i, newEntry)
                return mergeOverlappingRanges(ranges)
            }
        }
        return ranges
    }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val dataIdx = input.indexOfFirst { it.isEmpty() }
        val rangeInput = input.take(dataIdx)
        val result = mergeOverlappingRanges(
                rangeInput.map { createRange(it.trim()) }
                .sortedBy { it.first }
                .toMutableList()
        ).sumOf { it.second - it.first + 1 }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val dataIdx = input.indexOfFirst { it.isEmpty() }
        val rangeInput = input.take(dataIdx)
        val ranges = mergeOverlappingRanges(
            rangeInput.map { createRange(it.trim()) }
                .sortedBy { it.first }
                .toMutableList()
        )
        val data = input.subList(dataIdx + 1, input.size).map { it.trim().toLong() }.sortedBy { it }
        val result = data.count { entry -> ranges.any { entry >= it.first && entry <= it.second } }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
