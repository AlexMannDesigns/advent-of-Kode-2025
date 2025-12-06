package solutions

import println
import readInput
import readInputNoTrim

fun main() {
    fun addValues(a: Long, b: Long): Long = a + b

    fun multiplyValues(a: Long, b: Long): Long = a * b

    fun checkInBounds(input: List<String>, row: Int, col: Int): Boolean =
        row <= input.lastIndex && col <= input[row].lastIndex

    /**
     * gets the current 'block' of numbers as columns, delimited by a column of whitespace
     */
    fun getCurrentBlock(colIdx: Int, numberInput: MutableList<String>, limit: Int): Pair<MutableList<Long>, Int> {
        val numberBlock = mutableListOf<Long>()
        for (i in colIdx..limit) {
            val numberBuilder = StringBuilder()
            for (j in 0..numberInput.size) {
                if (checkInBounds(numberInput, j, i)) {
                    numberBuilder.append(numberInput[j][i])
                }
            }
            val number = numberBuilder.toString().trim()
            if (number.isNotEmpty()) {
                numberBlock.add(number.toLong())
            } else {
                return Pair(numberBlock, i)
            }
        }
        throw IllegalArgumentException("Should be unreachable")
    }

    fun computeBlock(operator: String, blockNumberList: MutableList<Long>): Long {
        var sum = blockNumberList.removeAt(0)
        for (number in blockNumberList) {
            sum = if (operator == "+") {
                addValues(sum, number)
            } else {
                multiplyValues(sum, number)
            }
        }
        return sum
    }

    fun part2(rawInput: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val input = rawInput.toMutableList()
        // with untrimmed input we end up with an empty line at the end which should be dropped
        input.removeAt(input.lastIndex)

        val operators = input.removeAt(input.lastIndex).trim().split("[\\s]+".toRegex())
        val numberInput = input.subList(0, input.size)
        val longestLineLength = numberInput.maxOfOrNull { line -> line.length } ?: throw Exception("Error in input")

        var total = 0L
        var operatorIdx = 0
        var idx = 0
        while (idx < longestLineLength) {
            val operator = operators[operatorIdx++]
            val (blockNumberList, i) = getCurrentBlock(idx, numberInput, longestLineLength)
            total += computeBlock(operator, blockNumberList)
            idx = i + 1
        }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return total
    }

    fun part1(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val numbersMatrix = input.map { line -> line.trim().split("[\\s]+".toRegex()).map { it.trim() } }

        var result = 0L
        for ((colIdx, _) in numbersMatrix[0].withIndex()) {
            val operation = numbersMatrix.last()[colIdx]
            var columnSum = numbersMatrix[0][colIdx].toLong()
            for (rowIdx in 1 until numbersMatrix.size - 1) {
                columnSum = if (operation == "+") {
                    addValues(columnSum, numbersMatrix[rowIdx][colIdx].toLong())
                } else {
                    multiplyValues(columnSum, numbersMatrix[rowIdx][colIdx].toLong())
                }
            }
            result += columnSum
        }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day06")
    val inputP2 = readInputNoTrim("Day06")
    part1(input).println()
    part2(inputP2).println()
}
