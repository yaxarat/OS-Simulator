internal object Hardware {
    // Simulator cycle control
    var infCycle = false
    var intCycle = 0

    // CPU clock control
    var clockCycle = 0
        private set

    // Memory control
    private val availableMemory = 2048
    var usedMemory = 0
        private set

    // Virtual Memory control
    private val availableVMemory = 1024
    var usedVMemory = 0
        private set

    fun tickCycle() {
        clockCycle++
    }

    fun getAvailableMemory(): Int {
        return availableMemory - usedMemory
    }

    fun borrowMemory(value: Int) {
        usedMemory += value
    }

    fun relieveMemory(value: Int) {
        usedMemory -= value
    }

    fun getAvailableVMemory(): Int {
        return availableVMemory - usedVMemory
    }

    fun borrowVMemory(value: Int) {
        usedVMemory += value
    }

    fun relieveVMemory(value: Int) {
        usedVMemory -= value
    }

    fun memoryCleaner() {
        if (ProgramHandler.readyQueue.size == 0) {
            usedMemory = 0
        }
    }
}