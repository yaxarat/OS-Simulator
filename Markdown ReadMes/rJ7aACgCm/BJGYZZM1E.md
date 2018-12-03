# Critical Section and Resolving Scheme
Critical sections are added to each process during it's generation
CS: Start of the Critical Section
CE: End of the Critical Section
```kotlin
while (commandLen < commandLimit) {
                val option = optionPicker()
                programTaskQueue.add(randPicker(450, 1700).toString())

                when (commandLen) {
                    crit1 -> {
                        programTaskQueue.add("CS")
                    }
                    crit2 -> {
                        programTaskQueue.add("CE")
                    }
                }

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
```

These critical section will later trigger the program's "in critical section" flair represented by a boolean.
```kotlin
class Program(val name: String, commandIn: ArrayList<String>, val size: Int) {
    var inCritical = false
    //...
}
```

Which is then used to determine how the program should be treated depending on their critical status by the Operator class.
```kotlin
    private fun process(currentProgram: Program) {
        if (currentProgram.argumentQueue.isEmpty() && currentProgram.runTime <= 0) {
            processor.setProgramState(State.EXIT)
            InterruptHandler.sendSignal(true)
        } else {
            processor.execute()
            currentQuantum++

            // Critical section resolving scheme
            if (currentQuantum > quantumLimit && !currentProgram.inCritical) {
                resetQuantum()
                processor.setProgramState(State.READY)
                InterruptHandler.sendSignal(true)
            } else if (currentQuantum > quantumLimit && currentProgram.inCritical) {
                resetQuantum()
            }
        }
    }
```
