# Multithread

Multithreading is achieved by using Kotlin's specific coroutine library. The home branch for threads are created and managed in the Main class.
```kotlin
import kotlinx.coroutines.*

object Main {
    //Toggle
    private val multiThread: Boolean = true

    // Main mainLoop for the simulator
    fun mainLoop() {
    //...
    }

    @JvmStatic
    fun main(args: Array<String>) {
        var input: String
        println("Enter exit to exit the program.")

        if(runManual) {
            if (multiThread) {
                var is1 = true
                do {
                    val read = Scanner(System.`in`)
                    print("Multi Console: ")

                    input = read.nextLine()
                    runBlocking {
                        when {
                            is1 -> {
                                coroutineScope {
                                    launch(Dispatchers.Default) {
                                        println("FROM: ${Thread.currentThread().name}")
                                        CLI.argIn(input)
                                        mainLoop()
                                        is1 = false
                                }
                                }
                            }
                            else -> {
                                coroutineScope {
                                    launch(Dispatchers.Default) {
                                        println("FROM: ${Thread.currentThread().name}")
                                        CLI.argIn(input)
                                        mainLoop()
                                        is1 = true
                                }
                                }
                            }
                        }
                    }
                } while (true)
            } else {
                do {
                    val read = Scanner(System.`in`)
                    print("Manual Console: ")

                    input = read.nextLine()
                    CLI.argIn(input)
                    mainLoop()
                } while (true)
            }
        } else {
        //...
        }
    }
}
```
Essentially, coroutines are light-weight threads. They are launched with launch coroutine builder in a context of some CoroutineScope. Here we are launching a new coroutine in the runBlocking, meaning that the lifetime of the new coroutine is limited by the lifetime of its coroutine scope.

In addition to the coroutine scope provided by different builders, it is possible to declare your own scope using coroutineScope builder. It creates new coroutine scope and does not complete until all launched children complete. The main difference between runBlocking and coroutineScope is that the latter does not block the current thread while waiting for all children to complete.

In Android OS, main thread can never be blocked and generated threads must be auto managed. Thus, the entirety of the main thread must also be separated into different branches when a command is entered and joined later after the completion of sub-branch thread.
```kotlin
    // Android GUI's entry point
    fun GUI() = runBlocking {
        // 4 threads
        val customDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
        
        // Launch threads
        GlobalScope.launch(customDispatcher) {
            do {
                mainLoop()
            } while (true)
        }
        (customDispatcher.executor as ExecutorService).shutdown()
    }
```
