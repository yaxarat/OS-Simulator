# Process implementation and PCB

Process handling and state switches are carried out by the PCB class
```kotlin
class PCB {
    val newQueue = ArrayList<Program>()
    val readyQueue = ArrayList<Program>()
    val waitQueue = ArrayList<Program>()
    val exitQueue = ArrayList<Program>()

    fun addReadyQueue(program: Program, borrowed: Boolean) {
        val availableMemory = Hardware.getAvailableMemory()

        // Check if current program will fit in available memory
        if (program.size <= availableMemory) {
            program.state = State.READY
            readyQueue.add(program)
            print("Added: " + program.pid + " and borrowed :" + program.size +"Mb\n")
            if (!borrowed) {
                Hardware.borrowMemory(program.size)
            }
        } else {
            program.state = State.NEW
            newQueue.add(program)
        }
    }

    fun addWaitQueue(program: Program) {
        program.state = State.BLOCKED
        waitQueue.add(program)
        this.readyQueue.remove(program)
    }

    fun updateQueues() {
        val availableMemory = Hardware.getAvailableMemory()
        val removalList = ArrayList<Program>()

        for (program in this.readyQueue) {
            if (program.state === State.EXIT) {
                removalList.add(program)
            }
        }

        if (!removalList.isEmpty()) {
            removeProgram(removalList)
        }

        if (availableMemory > 0 && this.waitQueue.size > 0) {
            loadWaitingProgram(availableMemory)
        } else if (availableMemory > 0 && this.newQueue.size > 0) {
            loadNewProgram(availableMemory)
        }
    }

    private fun removeProgram(programToRemove: ArrayList<Program>) {
        for (program in programToRemove) {
            print("Removed PID: " + program.pid + " and relieved :" + program.size +"Mb\n")
            Hardware.relieveMemory(program.size)
            exitQueue.add(program)
            this.readyQueue.remove(program)
        }
    }

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

    private fun loadNewProgram(availableMemory: Int) {
        for (index in newQueue.indices) {
            val program = newQueue[index]
            if (program.size < availableMemory) {
                this.addReadyQueue(program, false)
                this.newQueue.removeAt(index)
                break
            }
        }
    }

    private fun switchProgram() {
        // switch to programs that are not waiting for io
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
}
```
