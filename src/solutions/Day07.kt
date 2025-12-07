package solutions

import println
import readInput

fun main() {
    fun solveRecursive(grid: MutableList<String>, coords: Pair<Int, Int>, memo: MutableMap<Pair<Int, Int>, Long>): Long {
        val (r, c) = coords
        // if we have reached the end of the grid, we have completed a valid path and can return
        if (r >= grid.size) {
            return 1L
        }

        // if we have already explored from the current coords, we can just return the value from memo
        memo[coords]?.let { return it }

        // scan downwards until we hit a splitter or the end
        for (row in r until grid.size) {
            // if we go out of bounds horizontally this is not a valid path
            if (c !in grid[row].indices) {
                memo[coords] = 0L
                return 0L
            }
            // if we hit a splitter we need to go both ways, counting the number of valid paths
            if (grid[row][c] == '^') {
                val left  = solveRecursive(grid, row + 1 to c - 1, memo)
                val right = solveRecursive(grid, row + 1 to c + 1, memo)
                val total = left + right
                // store the computed values in our memo map to avoid repetition
                memo[coords] = total
                return total
            }
        }

        // If we reach the bottom, we add a valid path to the memoization and return 1
        memo[coords] = 1L
        return 1L
    }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val grid = input.toMutableList()

        // track explored positions with the number of possible paths beyond
        val memo = mutableMapOf<Pair<Int, Int>, Long>()

        // find our start position
        val startIdx = grid.removeAt(0).indexOfFirst { it == 'S' }

        val result = solveRecursive(grid, 0 to startIdx, memo)

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val grid = input.toMutableList()

        val startPosition = grid.removeAt(0).indexOfFirst { it == 'S' }

        val beamIndices = mutableSetOf(startPosition)
        val splitCounter = grid.sumOf { row ->
            row.withIndex().count { (idx, char) ->
                if (char == '^' && idx in beamIndices) {
                    beamIndices.remove(idx)
                    beamIndices.add(idx + 1)
                    beamIndices.add(idx - 1)
                    true
                } else {
                    false
                }
            }
        }
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return splitCounter
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
