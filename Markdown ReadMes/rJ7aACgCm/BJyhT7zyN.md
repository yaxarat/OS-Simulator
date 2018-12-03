# Virtual Memory with Paging
Virtual memory is represented by additional memory named as "availableVMemory" in the Hardware class and is manipulated similarly to the actual memory.
```kotlin
    // Virtual Memory control
    private val availableVMemory = 1024
    var usedVMemory = 0
        private set
        
    fun getAvailableVMemory(): Int {
        return availableVMemory - usedVMemory
    }

    fun borrowVMemory(value: Int) {
        usedVMemory += value
    }

    fun relieveVMemory(value: Int) {
        usedVMemory -= value
    }
```
One key difference, however, is the utilization of paging for all programs that are in the virtual memory. This is done by paging table simulated via Hashmap and it's various operations. It is implemented via PagingTable object:
```kotlin
object PagingTable {
    val pageTable: HashMap<Program, String> =  HashMap()

    private fun pageAddresser(input: Program): String {
        return "0x00${input.pid}"
    }

    fun page(input: Program) {
        val address = pageAddresser(input)

        pageTable[input] = address
    }

    fun dePage(input: Program) {
        pageTable.remove(input)
    }

    fun listTable(): ArrayList<String> {
        val keys = ArrayList<String>()
        for (programs in pageTable.keys) {
            keys.add(programs.name)
        }
        return keys
    }

    fun listVTavble(): ArrayList<String> {
        val values = ArrayList<String>()
        for (address in pageTable.values) {
            values.add(address)
        }
        return values
    }
}
```
Virtual memory is only used when main memory is out of space and more programs need to be stored. This decision is made by the PCB class:
```kotlin
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
```
