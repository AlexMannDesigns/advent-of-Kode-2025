package solutions

import println
import readInput

fun main() {
    fun List<CharArray>.inBounds(rowIndex: Int, colIndex: Int): Boolean =
        rowIndex in this.indices && colIndex in this[rowIndex].indices

    fun List<CharArray>.isObstruction(rowIndex: Int, colIndex: Int): Boolean =
        this.inBounds(rowIndex, colIndex) && this[rowIndex][colIndex] == '@'

    val neighbours: List<Pair<Int, Int>> = buildList {
        for (x in -1..1) for (y in -1..1)
            if (x != 0 || y != 0) add(Pair(x, y))
    }

    fun positionIsValid(grid: List<CharArray>, rowIndex: Int, colIndex: Int): Boolean {
        var obstructions = 0
        for ((x, y) in neighbours) {
            if (grid.isObstruction(rowIndex + y, colIndex + x) && ++obstructions >= 4) {
                return false
            }
        }
        return true
    }

    fun solveRecursive(grid: List<CharArray>, result: Int): Int {
        for ((rowIndex, row) in grid.withIndex()) {
            for ((colIndex, value) in row.withIndex()) {
                if (value == '@' && positionIsValid(grid, rowIndex, colIndex)) {
                    grid[rowIndex][colIndex] = '.'
                    return solveRecursive(grid, result + 1)
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val result = solveRecursive(input.map(String::toCharArray), 0)
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val inputAsArray = input.map(String::toCharArray)
        val result = inputAsArray.asSequence()
            .mapIndexed { rowIndex, row ->
                row.withIndex().count { (colIndex, char) ->
                    char == '@' && positionIsValid(inputAsArray, rowIndex, colIndex)
                }
        }.sum()

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
