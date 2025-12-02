package solutions

import println
import readInput

fun main() {
    fun String.splitInHalfOrNull(): Pair<String, String>? {
        if (this.isEmpty() || this.length % 2 != 0) {
            return null
        }
        val middleIndex = this.lastIndex / 2
        val firstHalf = this.substring(0..middleIndex)
        val secondHalf = this.substring(middleIndex + 1..this.lastIndex)
        return Pair(firstHalf, secondHalf)
    }

    fun String.splitByXOrNull(divisor: Int): List<String>? {
        if (this.isEmpty() || this.length % divisor != 0 || this.length == divisor) {
            return null
        }
        val result = mutableListOf<String>()
        for (offset in 0..this.lastIndex step divisor) {
            result.add(this.substring(offset, offset + divisor))
        }
        return result
    }

    fun part2(input: List<String>): Long {
        // all data is in first index of input
        val inputString = input.firstOrNull() ?: throw IllegalArgumentException("Invalid input $input")
        // split input by commas into a new list
        val inputList = inputString.split(",").map { it.trim() }
        // setup result list
        val result: MutableList<Long> = mutableListOf()
        // loop through the list, split by '-' and build a range with the first and last
        for (rangeAsString in inputList) {
            val rangeLimits = rangeAsString.split("-").map { it.trim().toLong() }
            // loop all numbers in range
            for (candidate in rangeLimits.first()..rangeLimits.last()) {
                // create string from candidate and break into a list of evenly sized strings
                // first, a list of singles, e.g. "123456" becomes [ "1", "2", "3", "4", "5", "6" ]
                // then pairs, i.e. [ "12", "34", "56" ]
                // trios... etc
                for (divisor in 1..(candidate.toString().length / 2)) {
                    val candidateList = candidate.toString().splitByXOrNull(divisor)
                    // if string can be evenly split by divisor, check all list entries match and add to result if so
                    if (candidateList != null && candidateList.all { it == candidateList.first() }) {
                        result.add(candidate)
                        break
                    }
                }
            }
        }
        // return the sum of the result list
        return result.sum()
    }

    fun part1(input: List<String>): Long {
        // all data is in first index of input
        val inputString = input.firstOrNull() ?: throw IllegalArgumentException("Invalid input $input")
        // split input by commas into a new list
        val inputList = inputString.split(",").map { it.trim() }
        // setup result list
        val result: MutableList<Long> = mutableListOf()
        // loop through the list, split by '-' and build a range with the first and last
        for (rangeAsString in inputList) {
            val rangeLimits = rangeAsString.split("-").map { it.trim().toLong() }
            for (candidate in rangeLimits.first()..rangeLimits.last()) {
                // for each number in range, convert the number to a string, and split into even halves if possible
                val candidateAsHalves = candidate.toString().splitInHalfOrNull()
                // if the halves are an exact match, add the candidate to the result list
                if (candidateAsHalves != null && candidateAsHalves.first == candidateAsHalves.second) {
                    result.add(candidate)
                }
            }
        }
        // return the sum of the result list
        return result.sum()
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
