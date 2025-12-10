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

private data class Rectangle(val a: Point, val b: Point, val c: Point, val d: Point, val area: Long) {
    companion object {
        fun fromOppositePoints(a: Point, b: Point, area: Long) =
            Rectangle(a, b, Point(a.x, b.y), Point(b.x, a.y), area)
    }
}

private data class VerticalEdge(val a: Point, val b: Point)

fun main() {
    fun calculateArea(a: Point, b: Point): Long {
        val first = if (a.x >= b.x) a.x - b.x + 1 else b.x - a.x + 1
        val second = if (a.y >= b.y) a.y - b.y + 1 else b.y - a.y + 1
        return first * second
    }

    fun pointInPolygon(point: Point, verticalEdges: List<VerticalEdge>): Boolean {
        var crossings = 0
        for (edge in verticalEdges) {
            if (point == edge.a || point == edge.b) {
                return true
            }
            val y1 = edge.a.y
            val y2 = edge.b.y

            if (point.y in minOf(y1, y2)..<maxOf(y1, y2) && point.x < edge.a.x) {
                crossings++
            }
        }
        return crossings % 2 == 1
    }


    fun checkPoints(rectangle: Rectangle, verticalEdges: List<VerticalEdge>): Boolean {
        val pointAInPolygon = pointInPolygon(rectangle.a, verticalEdges)
        val pointBInPolygon = pointInPolygon(rectangle.b, verticalEdges)
        val pointCInPolygon = pointInPolygon(rectangle.c, verticalEdges)
        val pointDInPolygon = pointInPolygon(rectangle.d, verticalEdges)
        return pointAInPolygon && pointBInPolygon && pointCInPolygon && pointDInPolygon
    }


    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point.fromList(line.split(',').map { it.toLong() }) }
        val rectangles = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b -> Rectangle.fromOppositePoints(a, b,calculateArea(a, b)) }
        }.sortedByDescending { it.area }

        val verticalEdges = points.mapIndexedNotNull { i, point ->
            if (i < points.lastIndex && point.x == points[i + 1].x) {
                VerticalEdge(point, points[i + 1])
            } else {
                null
            }
        }.toMutableList()

        if (points.first().x == points.last().x) {
            verticalEdges.add(VerticalEdge(points.last(), points.first()))
        }

        // TODO now add a check that none of the sides of the rectangle cross the polygon boundary
        // so, we do need the horizontal edges as well as the vertical ones
        val result = rectangles.first { rectangle -> checkPoints(rectangle, verticalEdges) }.area

//        val checkLargest = rectangles.first { it.a.x == 9L && it.a.y == 5L && it.b.x == 2L && it.b.y == 3L }
//        println(checkLargest)
//        isInPolygon(checkLargest, verticalEdges)
        // cast a ray from any corner not in the edges. If that ray crosses the edges an odd number of times, it is in the polygon
        // we need to create a new 'vertical edge' object, which contains adjacent points that have the same x value.
        // we use a list of VerticalEdge objects instead of every boundary point of the polygon.
        // then, for each rectangle, we take each of the 4 corner points and loop through the VerticalEdge list, checking:
        // if the corner x is less than the VerticalEdge x AND the corner y value is between the two VerticalEdge y values
        // ie: if (corner.x < vEdge.a.x && corner.y in minOf(vEdge.a.y, vEdge.b.y) until maxOf(vEdge.a.y, vEdge.b.y)) { intersection++ }
        // NB: each corner needs its own crossing count. If any corner is out of bounds, the rectangle is invalid.

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
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
    //part1(input).println()
    part2(input).println()
}
