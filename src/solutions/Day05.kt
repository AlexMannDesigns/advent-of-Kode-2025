package solutions

import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle
import println
import readInput
import kotlin.collections.withIndex

data class FreshIngredientIds(
    val ranges: MutableList<Pair<Long, Long>>,
    val data: MutableList<Long>,
    val highest: Long,
    val lowest: Long,
) {
    companion object {

        private fun createRange(codeRange: String): Pair<Long, Long> {
            val dashIdx = codeRange.indexOfFirst { it == '-' }
            return Pair(codeRange.take(dashIdx).toLong(), codeRange.substring(dashIdx + 1, codeRange.length).toLong())
        }

        fun generateDataFromInput(input: List<String>): FreshIngredientIds {
            val dataIdx = input.indexOfFirst { it.isEmpty() }
            val ranges = input.take(dataIdx).map { createRange(it.trim()) }.sortedBy { it.first }.toMutableList()
            val data = input.subList(dataIdx + 1, input.size).map { it.trim().toLong() }.toMutableList()
            data.sort()
            return FreshIngredientIds(
                ranges = ranges,
                data = data,
                highest = ranges.last().second,
                lowest = ranges.first().first,
            )
        }
    }
}


fun main() {

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return input.size.toLong()
    }

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val freshIngredients = FreshIngredientIds.generateDataFromInput(input)
        val lowestIdx = freshIngredients.data.indexOfFirst { it > freshIngredients.lowest }
        val highestIdx = freshIngredients.data.indexOfFirst { it > freshIngredients.highest }
        val truncatedData = freshIngredients.data.subList(lowestIdx, highestIdx)
        val result = truncatedData.count { entry -> freshIngredients.ranges.any { entry >= it.first && entry <= it.second } }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    //part2(input).println()
}
