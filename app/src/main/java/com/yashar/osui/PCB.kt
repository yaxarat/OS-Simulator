import com.yashar.osui.PagingTable
import java.util.ArrayList
import java.util.Collections

class PCB {
    val newQueue = ArrayList<Program>()
    val readyQueue = ArrayList<Program>()
    val waitQueue = ArrayList<Program>()
    val exitQueue = ArrayList<Program>()

    fun addReadyQueue(program: Program, borrowed: Boolean) {
        val availableMemory = Hardware.getAvailableMemory()
        val availableVMemory = Hardware.getAvailableVMemory()

        // Check if current program will fit in available memory
        if (program.size <= availableMemory) {
            program.state = State.READY
            readyQueue.add(program)
            if (!borrowed) {
                Hardware.borrowMemory(program.size)
            }
        } else if (program.size <= availableVMemory) {
            program.state = State.READY
            waitQueue.add(program)
            if (!PagingTable.pageTable.containsKey(program)) {
                PagingTable.page(program)
                Hardware.borrowVMemory(program.size)
            }
        }else {
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
        val availableVMemory = Hardware.getAvailableVMemory()
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
        } else if (availableVMemory > 0 && this.newQueue.size > 0) {
            loadNewProgram(availableVMemory)
        }
    }

    private fun removeProgram(programToRemove: ArrayList<Program>) {
        for (program in programToRemove) {
            Hardware.relieveMemory(program.size)
            exitQueue.add(program)
            this.readyQueue.remove(program)
        }
    }

    private fun loadWaitingProgram(availableMemory: Int) {
        for (index in waitQueue.indices) {
            val program = waitQueue[index]
            if (program.size < availableMemory) {
                if (PagingTable.pageTable.containsKey(program)) {
                    Hardware.relieveVMemory(program.size)
                    PagingTable.dePage(program)
                    this.addReadyQueue(program, false)
                    this.waitQueue.removeAt(index)
                    break
                } else {
                    this.addReadyQueue(program, true)
                    this.waitQueue.removeAt(index)
                    break
                }
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

    // RoundRobin
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