package solutions

import println
import readInput

fun main() {
    val validDirections = setOf('L', 'R')

    fun setTotalClicksOrNull(instruction: String): Int? = instruction.substring(1, instruction.length).toIntOrNull()

    fun setDirectionOrNull(instruction: String): Char? {
        val direction = instruction.firstOrNull() ?: return null
        if (direction !in validDirections) {
            return null
        }
        return direction
    }

    fun part2(input: List<String>): Int {
        var zeroCount = 0
        var dial = 50
        for (instruction in input) {
            // get direction and number of clicks from input line
            val direction = setDirectionOrNull(instruction) ?: throw IllegalArgumentException("Invalid input $instruction")
            var clicks = setTotalClicksOrNull(instruction) ?: throw IllegalArgumentException("Invalid input: $instruction")

            // we save the current dial position for a later check
            val dialStart = dial

            // if clicks is over 99, we need to decrement by 100 until it isn't
            // each 100 clicks is a full 'spin' and passes zero once each time
            while (clicks > 99) {
                clicks -= 100
                zeroCount++
            }

            // we then move the dial by the clicks amount
            if (direction == 'L') {
                clicks *= -1
            }
            dial += clicks

            // if the dial lands on zero, this is considered 'passing' so we increment
            // if dial has gone over 99 we have also passed 0, so we increment and normalize the value
            // if the dial is less than zero, but we didn't start there, we have also passed zero
            when {
                (dial == 0) -> zeroCount++
                (dial > 99) -> {
                    dial -= 100
                    zeroCount++
                }
                (dial < 0) -> {
                    dial += 100
                    if (dialStart != 0) {
                        zeroCount++
                    }
                }
            }
        }
        return zeroCount
    }

    fun setClicksOrNull(instruction: String): Int? {
        var clickInput = setTotalClicksOrNull(instruction) ?: return null
        while (clickInput > 99)
        {
            clickInput %= 100
        }
        return clickInput
    }

    fun part1(input: List<String>): Int {
        var zeroCount = 0
        var dial = 50
        for (instruction in input) {
            val direction = setDirectionOrNull(instruction) ?: throw IllegalArgumentException("Invalid input $instruction")
            val clicks = setClicksOrNull(instruction) ?: throw IllegalArgumentException("Invalid input: $instruction")
            when (direction) {
                'R' -> dial += clicks
                'L' -> dial -= clicks
                else -> throw IllegalArgumentException("Instruction '${instruction[0]}' unexpected")
            }
            when {
                (dial < 0) -> dial += 100
                (dial > 99) -> dial -= 100
            }
            if (dial == 0) zeroCount++
        }
        return zeroCount
    }

    // Read the input from the `src/input/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
