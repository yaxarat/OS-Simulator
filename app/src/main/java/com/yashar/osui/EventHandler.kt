import java.util.concurrent.ThreadLocalRandom

internal object EventHandler {
    private fun burst(): Int {
        return Hardware.clockCycle + ThreadLocalRandom.current().nextInt(25, 50 + 1)
    }

    fun scheduleIO(program: Program) {
        val interrupt = Interrupt(program, burst())
        InterruptHandler.addInterruptQueue(interrupt)
    }
}
