class Processor {
    private var currentProgram: Program? = null

    fun execute() {
        if (currentProgram!!.runTime == 0 && currentProgram!!.argumentQueue.size > 0) {
            val argument = currentProgram!!.argumentQueue.removeAt(0)
            val splitArgument = argument.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            when {
                argument.contains("CALCULATE") -> {
                    currentProgram!!.runTime = (Integer.valueOf(splitArgument[1]))
                    currentProgram!!.advanceRunTime()
                }
                argument.contains("IO") -> {
                    EventHandler.scheduleIO(currentProgram!!)
                    println(Formatter.ANSI_RED + "CURRENTLY BLOCKING : ${currentProgram.toString()}" + Formatter.ANSI_RESET)
                    this.setProgramState(State.BLOCKED)
                    InterruptHandler.sendSignal(true)
                }
                argument.contains("YIELD") -> {
                    this.setProgramState(State.READY)
                    InterruptHandler.sendSignal(true)
                }
                argument.contains("OUT") -> {
                    val pid = currentProgram!!.pid
                    println(Formatter.ANSI_YELLOW + "OUT REQUEST FROM PID: " + pid + Formatter.ANSI_RESET)
                    CLI.proc(pid)
                }
            }
        } else {
            currentProgram!!.advanceRunTime()
        }
    }

    fun getCurrentProgram(): Program? {
        return currentProgram
    }

    fun setCurrentProgram(program: Program) {
        this.currentProgram = program
        this.currentProgram!!.state = State.RUN
        Operator.resetQuantum()
    }

    fun setProgramState(stateIn: State) {
        if (currentProgram != null) {
            when (stateIn) {
                State.BLOCKED -> {
                    ProgramHandler.blockProgram(currentProgram!!)
                    this.currentProgram = null
                }
                State.EXIT -> {
                    currentProgram!!.state = (stateIn)
                    this.currentProgram = null
                }
                else -> currentProgram!!.state = (stateIn)
            }
        }
    }
}