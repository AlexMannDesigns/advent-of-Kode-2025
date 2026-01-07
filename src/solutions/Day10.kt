package solutions

import println
import readInput

// converts int list of 0s and 1s to the number it represents as binary
private fun List<Int>.binaryListAsInt() = this.joinToString(separator = "").toInt(2)

private data class Button(val button: List<Int>, val sum: Int)

private data class PressSequence(val result: Int, val presses: Int)

private data class Buttons(val buttonList: List<Button>, val asBinary: List<Int>) {
    companion object {
        private fun createButtonsBinaryList(line: String): List<List<Int>> =
            line.substring(line.indexOfFirst { it == '(' } + 1, line.indexOfLast { it == ')' })
                .split(") (")
                .map { buttonString -> buttonString.split(',').let { it.map { c -> c.toInt() } } }

        private fun buttonAsBinary(button: List<Int>, lightsSize: Int): List<Int> =
            (0..<lightsSize).map { i -> if (i in button) 1 else 0 }


        fun fromInputLine(line: String, lightsSize: Int): Buttons {
            val buttons = createButtonsBinaryList(line).map { button ->
                val buttonList = buttonAsBinary(button, lightsSize)
                Button(buttonList, buttonList.sum())
            }.sortedByDescending { it.sum }
            val buttonsAsBinary = buttons.map { button -> button.button.binaryListAsInt() }
            return Buttons(buttons, buttonsAsBinary)
        }
    }
}

private class Machine(val lights: Int, val buttons: Buttons, var jolts: MutableList<Int>, var presses: Int = 0) {
    fun buttonCanBePressed(button: List<Int>): Boolean {
        button.forEachIndexed { index, buttonValue -> if (buttonValue == 1 && jolts[index] == 0) { return false } }
        return true
    }

    fun multiPressButton(button: List<Int>, pressCount: Int) {
        button.forEachIndexed { index, buttonValue -> jolts[index] -= (buttonValue * pressCount) }
        presses += pressCount
    }

    fun multiUnpressButton(button: List<Int>, pressCount: Int) {
        button.forEachIndexed { index, buttonValue -> jolts[index] += (buttonValue * pressCount) }
        presses -= pressCount
    }

    fun buttonEffectsJoltIndex(joltIndex: Int, button: List<Int>): Boolean = button[joltIndex] == 1

    fun joltsSolved() = jolts.all { it == 0 }

    fun lowestNonZeroJoltIdx(): Int = jolts.withIndex().filter { it.value != 0 }.minByOrNull { it.value }?.index ?:
        throw IllegalArgumentException("Invalid jolts in machine: $this")

    fun getNonZeroJoltIndicesAsList() = jolts.withIndex().filter { it.value != 0 }.map { it.index }.sorted()

    companion object {
        private fun createLightsBinaryList(line: String): List<Int> =
            line.substring(line.indexOfFirst { it == '[' } + 1, line.indexOfFirst { it == ']' }).map { if (it == '#') 1 else 0 }

        private fun createJolts(line: String): MutableList<Int>  =
            line.substring(line.indexOfFirst { it == '{' } + 1, line.indexOfLast { it == '}' })
                .split(",")
                .map { i -> i.toInt() }
                .toMutableList()

        fun fromInputLine(line: String): Machine {
            val lights = createLightsBinaryList(line)
            val buttons = Buttons.fromInputLine(line, lights.size)
            val jolts = createJolts(line)
            return Machine(
                lights = lights.binaryListAsInt(),
                buttons = buttons,
                jolts = jolts
            )
        }
    }
}


fun main() {

    fun solveMinJoltPresses(machine: Machine): Boolean {
        val joltIdx = machine.lowestNonZeroJoltIdx()
        val pressCount = machine.jolts[joltIdx]

        for (b in machine.buttons.buttonList) {
            val button = b.button
            if (machine.buttonEffectsJoltIndex(joltIdx, button) && machine.buttonCanBePressed(button)) {
                machine.multiPressButton(button, pressCount)
                if (machine.joltsSolved() || solveMinJoltPresses(machine)) {
                    return true
                }
                machine.multiUnpressButton(button, pressCount)
            }
        }
        return false
    }

    fun part2(input: List<String>): Int {
        val startTime = System.currentTimeMillis()
        val machinesList = input.map { line -> Machine.fromInputLine(line) }
//        for (machine in machinesList) {
//            println("${machine.buttons.buttonList} || ${machine.jolts}")
//        }

        val result = machinesList.sumOf { machine ->
            solveMinJoltPresses(machine)
            machine.presses
        }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        return result
    }

    fun createCombos(buttonMatrix: List<Int>, start: Int, current: MutableList<Int>, result: MutableList<PressSequence>): List<PressSequence> {
        if (current.isNotEmpty()) {
            val presses = current.size
            val sequenceResult = current.reduce { a, b -> a xor b }
            result.add(PressSequence(sequenceResult, presses))
        }

        for (i in start..buttonMatrix.lastIndex) {
            current.add(buttonMatrix[i])
            createCombos(buttonMatrix, i + 1, current, result)
            current.removeLast()
        }
        return result
    }

    fun calculateMinPresses(machine: Machine) =
        createCombos(machine.buttons.asBinary, 0, mutableListOf(), mutableListOf())
            .sortedBy { it.presses }
            .first { combo -> combo.result == machine.lights }.presses

    fun part1(input: List<String>): Int {
        val startTime = System.currentTimeMillis()

        val result = input.map { line -> Machine.fromInputLine(line) }.sumOf { machine -> calculateMinPresses(machine) }

        println("Time taken: ${System.currentTimeMillis() - startTime} ms.")
        // the result to return is the sum of the fewest number of button presses to turn on each machine
        return result
    }

    // Read the input from the `src/input/Day0x.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
