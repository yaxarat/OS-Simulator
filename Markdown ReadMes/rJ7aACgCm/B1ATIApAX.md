# I/O interrupts and handlers
Interrupt itself is designed as below in the Interrupt class. Where program will have certain interrupt cycle length.
```kotlin
class Interrupt(var program: Program, var length: Int)
```
Interrupts can be caused by I/O, and the interrupt is generated via IO class. Where IO scheduler will create an IO burst and add it to the interrrupt queue.
```kotlin
object IO {
    private fun burst(): Int {
        return Hardware.clockCycle + ThreadLocalRandom.current().nextInt(25, 50 + 1)
    }

    fun scheduleIO(program: Program) {
        val interrupt = Interrupt(program, burst())
        InterruptHandler.addInterruptQueue(interrupt)
    }
}
```
Interrupt queue is represented by a priority queue where **first come first serve algorithm** is used in InterruptHandler class.
```kotlin
object InterruptHandler {
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
```
