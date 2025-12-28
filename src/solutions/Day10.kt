package solutions

import println
import readInput

private data class Machine(val lights: Int, val buttons: List<Int>) {
    companion object {
        // converts int list of 0s and 1s to the number it represents as binary
        private fun List<Int>.binaryListAsInt() = this.joinToString(separator = "").toInt(2)

        private fun createLightsBinaryList(line: String): List<Int> =
            line.substring(line.indexOfFirst { it == '[' } + 1, line.indexOfFirst { it == ']' }).map { if (it == '#') 1 else 0 }

        private fun createButtonsBinaryList(line: String): List<List<Int>> =
            line.substring(line.indexOfFirst { it == '(' } + 1, line.indexOfLast { it == ')' })
                .split(") (")
                .map { buttonString -> buttonString.split(',').let { it.map { c -> c.toInt() } } }

        private fun buttonAsBinaryList(button: List<Int>, lightsSize: Int): List<Int> =
            (0..<lightsSize).map { i -> if (i in button) 1 else 0 }

        fun fromInputLine(line: String): Machine {
            val lights = createLightsBinaryList(line)
            return Machine(
                lights = lights.binaryListAsInt(),
                buttons = createButtonsBinaryList(line).map { button -> buttonAsBinaryList(button, lights.size).binaryListAsInt() },
            )
        }
    }
}

data class PressSequence(val result: Int, val presses: Int)

fun main() {
    fun createCombos(buttonMatrix: List<Int>, start: Int, current: MutableList<Int>, result: MutableList<PressSequence>): List<PressSequence> {
        if (current.isNotEmpty()) {
            val presses = current.size // num of presses in sequence
            val sequenceResult = current.reduce { a, b -> a xor b } // xor buttons together to create result
            result.add(PressSequence(sequenceResult, presses))
        }

        for (i in start..buttonMatrix.lastIndex) {
            current.add(buttonMatrix[i])
            createCombos(buttonMatrix, i + 1, current, result)
            current.removeLast()
        }
        return result.sortedBy { it.presses }
    }

    fun calculateMinPresses(machine: Machine) =
        createCombos(machine.buttons, 0, mutableListOf(), mutableListOf())
            .first { combo -> combo.result == machine.lights }.presses

//    fun part2(input: List<String>): Long {
//        val startTime = System.currentTimeMillis()
//
//        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
//        return input.size.toLong()
//    }

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        // parse input into a list of data classes
        val result = input.map { line -> Machine.fromInputLine(line) }.sumOf { machine -> calculateMinPresses(machine) }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        // the result to return is the sum of the fewest number of button presses to turn on each machine
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()
}
