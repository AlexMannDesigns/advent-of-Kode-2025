package solutions

import println
import readInput

fun main() {
    fun solve(numberList: List<Int>, count: Int): Long {
        var numberListCopy = numberList
        var final: Long = 0
        for (n in count downTo 0) {
            val max = numberListCopy.subList(0, numberListCopy.size - n).max()
            val sliceIndex = numberListCopy.indexOfFirst { it == max } + 1
            numberListCopy = numberListCopy.subList(sliceIndex, numberListCopy.size)
            final = final * 10 + max
        }
        return final
    }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val result = input.sumOf { line ->
            solve(line.map { char -> char.digitToInt() }, 11)
        }
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    fun part1(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val result = input.sumOf { line ->
            solve(line.map { char -> char.digitToInt() }, 1)
        }
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
