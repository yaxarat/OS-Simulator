# Basic memory and operations
Memory resides within Hardware class and is implemented as below:
```kotlin
    // Memory control
    private val availableMemory = 2048
    var usedMemory = 0
        private set

    fun getAvailableMemory(): Int {
        return availableMemory - usedMemory
    }

    fun borrowMemory(value: Int) {
        usedMemory += value
    }

    fun relieveMemory(value: Int) {
        usedMemory -= value
    }
```
