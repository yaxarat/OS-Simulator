# Process Priorities and Multilevel Scheduling
Priority of a process is determined through process aging. Each process has an aging value represented by `var priority = 0` .

This value is then used by the operator class to determine how much time quantum is allocated to a certain process. Effectively recreating a multilevel scheduling.
```kotlin
if (currentQuantum > quantumLimit && !currentProgram.inCritical) {
        when (currentProgram.priority) {
                0 -> {
                    resetQuantum()
                    processor.setProgramState(State.READY)
                    InterruptHandler.sendSignal(true)
                    currentProgram.priority++
                }
                else -> {
                    resetQuantumPriority(currentProgram.priority * (-15))
                    currentProgram.priority++
                }
            }
        } else if (currentQuantum > quantumLimit && currentProgram.inCritical) {
            resetQuantum()
        }
```
