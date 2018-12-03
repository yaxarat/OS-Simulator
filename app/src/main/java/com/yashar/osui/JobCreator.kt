package com.yashar.osui

import java.util.*
import java.util.concurrent.ThreadLocalRandom

object JobCreator {
    private val programTaskQueue = ArrayList<String>()
    private var globalCount = 0
    val random = Random()

    private fun optionPicker(): Int {
        return ThreadLocalRandom.current().nextInt(0, 3 + 1)
    }

    private fun randPicker(from: Int, to: Int) : Int {
        return random.nextInt(to - from) + from
    }

    fun makeProgram(numProcess: Int) {
        var count = 0

        while (count < numProcess) {
            var commandLen = 0

            while (commandLen < randPicker(5, 15)) {
                val option = optionPicker()
                programTaskQueue.add(randPicker(450, 1700).toString())

                when (option) {
                    0 -> {
                        programTaskQueue.add("CALCULATE ${randPicker(100, 350)}")
                    }
                    1 -> {
                        programTaskQueue.add("IO")
                    }
                    2 -> {
                        programTaskQueue.add("YIELD")
                    }
                    3 -> {
                        programTaskQueue.add("OUT")
                    }
                }
                commandLen++
            }
            println(programTaskQueue)
            CLI.autoLoad("Program$globalCount", programTaskQueue)
            programTaskQueue.clear()
            count++
            globalCount++
        }
    }
}