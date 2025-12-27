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

private data class Rectangle(
    val a: Point,
    val b: Point,
    val c: Point,
    val d: Point,
    val area: Long,
    val xRangeStart: Long,
    val xRangeEnd: Long,
    val yRangeStart: Long,
    val yRangeEnd: Long
) {
    companion object {
        private fun calculateArea(a: Point, b: Point): Long =
            (maxOf(a.x, b.x) - minOf(a.x, b.x) + 1) * (maxOf(a.y, b.y) - minOf(a.y, b.y) + 1)

        fun fromOppositePoints(a: Point, b: Point): Rectangle {
            val i = Point(a.x, b.y)
            val j = Point(b.x, a.y)

            val xRangeStart = minOf(a.x, b.x) + 1
            val xRangeEnd = maxOf(a.x, b.x) - 1
            val yRangeStart = minOf(a.y, b.y) + 1
            val yRangeEnd = maxOf(a.y, b.y) - 1

            val area = calculateArea(a, b)

            return Rectangle(a, i, b, j, area, xRangeStart, xRangeEnd, yRangeStart, yRangeEnd)
        }
    }
}

private data class Edge(val a: Point, val b: Point)

fun main() {
    fun rangesOverlap(aStart: Long, aEnd: Long, bStart: Long, bEnd: Long) = aStart <= bEnd && bStart <= aEnd

    fun coordInRange(coord: Long, rangeStart: Long, rangeEnd: Long) = coord in rangeStart..rangeEnd

    fun hasOverlap(
        edgeRangeStart: Long,
        edgeRangeEnd: Long,
        edgeCoord: Long,
        rectangleRange1Start: Long,
        rectangleRange1End: Long,
        rectangleRange2Start: Long,
        rectangleRange2End: Long
    ) = rangesOverlap(edgeRangeStart, edgeRangeEnd, rectangleRange1Start, rectangleRange1End)
                && coordInRange(edgeCoord, rectangleRange2Start, rectangleRange2End)

    fun hasNoOverlaps(rectangle: Rectangle, verticalEdges: List<Edge>, horizontalEdges: List<Edge>) =
        !(horizontalEdges.any { edge -> hasOverlap(
            minOf(edge.a.x, edge.b.x),
            maxOf(edge.a.x, edge.b.x),
            edge.a.y,
            rectangle.xRangeStart,
            rectangle.xRangeEnd,
            rectangle.yRangeStart,
            rectangle.yRangeEnd,
        )} || verticalEdges.any { edge -> hasOverlap(
            minOf(edge.a.y, edge.b.y),
            maxOf(edge.a.y, edge.b.y),
            edge.a.x,
            rectangle.yRangeStart,
            rectangle.yRangeEnd,
            rectangle.xRangeStart,
            rectangle.xRangeEnd,
        )})

    fun createBoundaryEdgeLists(points: List<Point>): Pair<List<Edge>, List<Edge>> =
        points.zipWithNext()
            .plus(points.last() to points.first())
            .map { (a, b) -> Edge(a, b) }
            .partition { edge -> edge.a.x == edge.b.x }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point.fromList(line.split(',').map { it.toLong() }) }

        val (verticalEdges, horizontalEdges) = createBoundaryEdgeLists(points)

        val result = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b ->
                Rectangle.fromOppositePoints(a, b)
            }
        }.sortedByDescending { it.area }
            .first { rectangle ->
                hasNoOverlaps(rectangle, verticalEdges, horizontalEdges)
        }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result.area
    }

    fun part1(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point.fromList(line.split(',').map { it.toLong() }) }
        val result = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b ->
                Rectangle.fromOppositePoints(a, b)
            }
        }.maxByOrNull { it.area }?.area
            ?: throw IllegalArgumentException("No result found, check input")

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day09")
    val testInput = readInput("test_Day09")
    part1(input).println()
    part2(input).println()
}
