internal class Operator {
    private val programHandler = ProgramHandler()
    private val processor = Processor()

    fun run() {
        val currentProgram = processor.getCurrentProgram()
        update(currentProgram)
        // Only continue processing if program exist in the ready queue
        if (currentProgram != null) {
            process(currentProgram!!)
        }
    }

    private fun update(currentProgram: Program?) {
        // Update current queue by removing finished and adding waiting program
        programHandler.update()

        // Switch program if no program is currently running or the current program has finished its quantum
        if (InterruptHandler.signal || currentProgram == null) {
            val next = programHandler.nextProgram

            if (next != null) {
                processor.setCurrentProgram(next)
                processor.setProgramState(State.RUN)
            }
        }
        InterruptHandler.sendSignal(false)
        Hardware.tickCycle()

        // Check for new io interrupt
        if (hasIoComplete()) {
            probe(currentProgram)
        }
    }

    private fun probe(currentProgram: Program?) {
        if (currentProgram != null) {
            processor.setProgramState(State.READY)
        }
        val interrupt = InterruptHandler.getInterruptQueue()
        processor.setCurrentProgram(interrupt!!.program)
        processor.setProgramState(State.RUN)
    }

    private fun process(currentProgram: Program) {
        if (currentProgram.argumentQueue.isEmpty() && currentProgram.runTime <= 0) {
            processor.setProgramState(State.EXIT)
            InterruptHandler.sendSignal(true)
        } else {
            processor.execute()
            currentQuantum++

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
        }
    }

    companion object {
        private val quantumLimit = 15
        private var currentQuantum = 0

        private fun hasIoComplete(): Boolean {
            return null != InterruptHandler.peek() && InterruptHandler.peek()?.length!! < Hardware.clockCycle
        }

        fun resetQuantum() {
            currentQuantum = 0
        }
        fun resetQuantumPriority(num: Int) {
            currentQuantum = num
        }
    }
}
