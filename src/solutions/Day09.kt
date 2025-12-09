package solutions

import println
import readInput

private data class Point(val x: Long, val y: Long) {
    companion object {
        fun fromList(list: List<Long>): Point {
            if (list.isEmpty() || list.size != 2) {
                throw IllegalArgumentException("list invalid")
            }
            return Point(list.first(), list.last())
        }
    }
}

fun main() {
    fun calculateArea(a: Point, b: Point): Long {
        val first = if (a.x >= b.x) a.x - b.x + 1 else b.x - a.x + 1
        val second = if (a.y >= b.y) a.y - b.y + 1 else b.y - a.y + 1
        return first * second
    }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point.fromList(line.split(',').map { it.toLong() }) }
        val areas = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b -> calculateArea(a, b) }
        }.sorted()

        // the twist now is that we need to check that all points within the area are 'in bounds'
        // first we need to determine where the boundaries are
        // create a list of 'in bounds' points and iterate through all points to draw lines between them, adding points as we go.
        // We only need to check two direction from each point, as we are only interested in the points between them.
        // we need to check if point B is either north or south and west or east from point A by diff-ing the x-y coords.
        // we then start by, for example, North from point A, checking the in-bounds list for each point as we decrement 'x'
        // if we hit an out-of-bounds point, we must perform an in-bounds check.
        // We iterate north, south, east, and west and if we hit a boundary on all sides, we are in-bounds and add the point to the list
        // If we find that have indeed gone out-of-bounds, we stop immediately and can dismiss this as a valid area
        // Optimisation idea: binary search through the list to minimise the number of checks...?


        println(areas.size)
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return input.size.toLong()
    }

    fun part1(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point.fromList(line.split(',').map { it.toLong() }) }
        val areas = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b -> calculateArea(a, b) }
        }.sorted()

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return areas.last()
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
