package solutions

import println
import readInput
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

private data class Point3D(val x: Double, val y: Double, val z: Double) {
    companion object {
        fun fromList(list: List<Int>): Point3D {
            if (list.isEmpty() || list.size != 3) {
               throw IllegalArgumentException("List invalid: $list")
            }
            return Point3D(list[0].toDouble(), list[1].toDouble(), list[2].toDouble())
        }
    }
}

private data class Distance(val point1: Point3D, val point2: Point3D, val distance: Int)

fun main() {
    fun calculateEuclideanDistance(a: Point3D, b: Point3D): Int =
        sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2) + (a.z - b.z).pow(2)).roundToInt()

    fun matchingPoints(a: Distance, b: Distance): Boolean =
        a.point1 == b.point1 || a.point2 == b.point2 || a.point1 == b.point2 || a.point2 == b.point1

    fun getFinalConnection(points: MutableList<Point3D>, distances: List<Distance>): Distance {
        for (d in distances) {
            points.removeAll { d.point1 == it || d.point2 == it }
            if (points.isEmpty()) {
                return d
            }
        }
        throw IllegalArgumentException("Should not happen")
    }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point3D.fromList(line.split(',').map { it.toInt() }) }.toMutableList()

        val distances = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b ->
                Distance(a, b, calculateEuclideanDistance(a, b))
            }
        }.sortedBy { it.distance }

        val finalConnection = getFinalConnection(points, distances)
        val result = finalConnection.point1.x.toLong() * finalConnection.point2.x.toLong()

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // TODO do this a tree, rather than a matrix
    // https://dev.to/wolfof420street/trees-in-kotlin-a-comprehensive-guide-2fea
    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point3D.fromList(line.split(',').map { it.toInt() }) }

        // calculate euclidian distances between each of the circuits and then sort ascending
        val distances = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b ->
                Distance(a, b, calculateEuclideanDistance(a, b))
            }
        }.sortedBy { it.distance }

        val circuits = mutableListOf<MutableList<Distance>>()
        for (d in distances.take(1000)) {
            // Find all circuits that share a point with this distance
            val overlapping = circuits.filter { circuit ->
                circuit.any { existing ->
                    matchingPoints(existing, d)
                }
            }

            when (overlapping.size) {
                0 -> circuits.add(mutableListOf(d))
                1 -> overlapping.first().add(d)
                else -> {
                    // if multiple circuits match, they need to be merged together
                    val merged = mutableListOf<Distance>()
                    for (c in overlapping) {
                        merged.addAll(c)
                    }
                    merged.add(d)
                    circuits.removeAll(overlapping)
                    circuits.add(merged)
                }
            }
        }
        // we then need to count the unique points in each circuit, and multiply the quantity of the 3 largest
        val result = circuits.map { circuit ->
            circuit.flatMap { listOf(it.point1, it.point2) }.toSet()
        }
            .sortedByDescending { it.size }
            .take(3)
            .map { it.size }
            .reduce { a, b -> a * b }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
