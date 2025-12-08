package solutions

import println
import readInput
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class Point3D(val x: Double, val y: Double, val z: Double) {
    companion object {
        fun fromList(list: List<Int>): Point3D {
            if (list.isEmpty() || list.size != 3) {
               throw IllegalArgumentException("List invalid: $list")
            }
            return Point3D(list[0].toDouble(), list[1].toDouble(), list[2].toDouble())
        }
    }
}

data class Distance(val point1: Point3D, val point2: Point3D, val distance: Int)

fun main() {
    fun calculateEuclideanDistance(a: Point3D, b: Point3D): Int =
        sqrt((a.x - b.x).pow(2) + (a.y - b.y).pow(2) + (a.z - b.z).pow(2)).roundToInt()

    fun matchingPoints(a: Distance, b: Distance): Boolean =
        a.point1 == b.point1 || a.point2 == b.point2 || a.point1 == b.point2 || a.point2 == b.point1

    fun getFinalConnection(points: MutableList<Point3D>, distances: List<Distance>): Distance {
        // iterate through distances and remove each point encountered from the point list until it gets to 0
        // once the last unique point is found, it is that connection where we need to multiply the two x values together
        // this gives us the result.

        for (d in distances) {
            val indexPoint1 = points.indexOfFirst { d.point1 == it }
            if (indexPoint1 != -1) {
                points.removeAt(indexPoint1)
            }
            val indexPoint2 = points.indexOfFirst { d.point2 == it }
            if (indexPoint2 != -1) {
                points.removeAt(indexPoint2)
            }
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
        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return finalConnection.point1.x.toLong() * finalConnection.point2.x.toLong()
    }

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val points = input.map { line -> Point3D.fromList(line.split(',').map { it.toInt() }) }

        // calculate euclidian distances between each of the circuits and then sort ascending
        val distances = points.flatMapIndexed { index, a ->
            points.drop(index + 1).map { b ->
                Distance(a, b, calculateEuclideanDistance(a, b))
            }
        }.sortedBy { it.distance }

//        for (distance in distances) {
//            println("${distance.point1}, ${distance.point2}, ${distance.distance}")
//        }

        // now we need to group them into circuits...
        // circuits could be a list of lists, where for each distance, we search each list to see if either point
        // is already there, then add it to that list. Otherwise, we create a new list

        val circuits = mutableListOf<MutableList<Distance>>()
        for (d in distances.take(1000)) {
            // Find all circuits that share a point with this distance
            val overlapping = circuits.filter { circuit ->
                circuit.any { existing -> matchingPoints(existing, d) }
            }

            when (overlapping.size) {
                0 -> {
                    // create new circuit if no match was found
                    circuits.add(mutableListOf(d))
                }
                1 -> {
                    // Exactly one matching circuit, just add to it
                    overlapping.first().add(d)
                }
                else -> {
                    // Multiple circuits match, they need to be merged together
                    val merged = mutableListOf<Distance>()
                    for (c in overlapping) {
                        merged.addAll(c)
                    }
                    merged.add(d)

                    // remove old circuits
                    circuits.removeAll(overlapping)
                    // add merged circuit
                    circuits.add(merged)
                }
            }
        }
        // we then need to count the unique points
        val uniquePointsPerCircuit = circuits.map { circuit -> circuit.flatMap { listOf(it.point1, it.point2) }.toSet() }.sortedByDescending { it.size }

        // multiply quantity of points in the 3 largest circuits together to get the result
        val result = uniquePointsPerCircuit.take(3).map { it.size }.reduce { a, b -> a * b }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day08")
    //part1(input).println()
    part2(input).println()
}
