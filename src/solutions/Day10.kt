package solutions

import println
import readInput

private data class Machine(val lights: List<Int>, val buttons: List<List<Int>>)

fun main() {
    fun calculatePresses(lights: List<Int>, buttonMatrix: List<List<Int>>): Int {
        // first we see if any individual button press gets us to the solution
        if (buttonMatrix.any { it == lights }) return 1

        // then we try any two buttons together, then 3, 4 etc
        for (numButtons in 2..buttonMatrix.size) {
            println(numButtons)
            // sum columns for every button combo
            // TODO research a good algorithm for this
            // if we have odd or even values in the correct positions, we return num buttons

        }
        throw IllegalArgumentException("No valid combo of buttons available for lights: $lights and buttons: $buttonMatrix")
    }

    fun part2(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return input.size.toLong()
    }

    fun part1(input: List<String>): Long {
        val startTime = System.currentTimeMillis()

        // parse input into a list of data classes
        val machinesList = input.map { line ->
            Machine(
                lights = line
                .substring(line.indexOfFirst { it == '[' } + 1, line.indexOfFirst { it == ']' })
                .map { if (it == '#') 1 else 0 },
                buttons = line
                .substring(line.indexOfFirst { it == '(' } + 1, line.indexOfLast { it == ')' })
                .split(") (")
                .map { buttonString -> buttonString.split(',').let { it.map { c -> c.toInt() } } }
            )
        }

        // let's organise the data into a matrix with row of 0s and 1s
        // we are trying to find the smallest combo of rows, whose columns have an odd number where lights should be on
        // and even number where lights should be off (note: 0 is an even number)
        val result = machinesList.sumOf { machine ->
            calculatePresses(
                machine.lights,
                machine.buttons.map { button -> (0..<machine.lights.size).map { i -> if (i in button) 1 else 0 } },
            )
        }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        // the result to return is the sum of the fewest number of button presses to turn on each machine
        return result.toLong()
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("test_Day10")
    part1(input).println()
//    part2(input).println()
}
