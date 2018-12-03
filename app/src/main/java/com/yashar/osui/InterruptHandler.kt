import java.util.Comparator
import java.util.PriorityQueue

internal object InterruptHandler {
    // Ensure program with least amount of wait length is processed next
    private val interrupts = PriorityQueue<Interrupt>(Comparator.comparingInt<Interrupt> { interrupt -> interrupt.length })
    var signal = false
        private set

    fun addInterruptQueue(thisInterrupt: Interrupt) {
        interrupts.add(thisInterrupt)
    }

    fun getInterruptQueue(): Interrupt? {
        return interrupts.poll()
    }

    fun sendSignal(signal: Boolean) {
        InterruptHandler.signal = signal
    }

    fun peek(): Interrupt? {
        return interrupts.peek()
    }
}
