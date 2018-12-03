# Scheduler and Scheduling Algorithms
Scheduling for all process are handled by ProgramHandler and PCB class
```kotlin
class Scheduler {

    val nextProgram: Program?
        get() = pcb.next()

    fun update() {
        Scheduler.pcb.updateQueues()
    }

    companion object {
        private val pcb = PCB()

        fun insertProgram(program: Program) {
            program.setArrivalTime(Hardware.clockCycle)
            pcb.addReadyQueue(program, false)
        }

        fun blockProgram(program: Program) {
            pcb.addWaitQueue(program)
        }

        val newQueue: ArrayList<Program>
            get() = pcb.newQueue

        val readyQueue: ArrayList<Program>
            get() = pcb.readyQueue

        val waitQueue: ArrayList<Program>
            get() = pcb.waitQueue

        val exitQueue: ArrayList<Program>
            get() = pcb.exitQueue
    }
}
```

Different scheduling algorithms are employed throughout the program.

Round Robin:
```kotlin
    // RoundRobin
    operator fun next(): Program? {
        switchProgram()

        // Get next available program
        for (program in this.readyQueue) {
            if (program.state !== State.BLOCKED) {
                return program
            }
        }
        return null
    }
    
    private fun switchProgram() {
        // switch to programs after every 15 quantum that are not waiting for io
        var rotate = false
        for (program in this.readyQueue) {
            if (program.state !== State.BLOCKED) {
                rotate = true
            }
        }
        if (rotate) {
            Collections.rotate(this.readyQueue, 1)
        }
    }
    
    // If waiting for IO, program is sent to waiting queue
    fun addWaitQueue(program: Program) {
        program.state = State.BLOCKED
        waitQueue.add(program)
        this.readyQueue.remove(program)
    }
```

First Come First Serve:
```kotlin
    // First program that fit in memory from the wait queue is removed from wait queue and added back into the ready queue
    private fun loadWaitingProgram(availableMemory: Int) {
        for (index in waitQueue.indices) {
            val program = waitQueue[index]
            if (program.size < availableMemory) {
                this.addReadyQueue(program, true)
                this.waitQueue.removeAt(index)
                break
            }
        }
    }
```
