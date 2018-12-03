import java.util.ArrayList

class ProgramHandler {

    val nextProgram: Program?
        get() = pcb.next()

    fun update() {
        ProgramHandler.pcb.updateQueues()
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