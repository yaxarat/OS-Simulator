import com.yashar.osui.JobCreator
import java.util.Scanner
import java.util.ArrayList
import java.io.File
import kotlinx.coroutines.*


object Main {
    private val oprt = Operator()
    private val manual: Boolean = false
    private val multiThread: Boolean = true

    // Main mainLoop for the simulator
    private fun mainLoop() {
        Hardware.memoryCleaner()
        if (!Hardware.infCycle && Hardware.intCycle == 0) {
            print("")
        } else if (Hardware.infCycle) {
            while (!ProgramHandler.readyQueue.isEmpty() || !ProgramHandler.waitQueue.isEmpty()) {
                oprt.run()
                Hardware.intCycle--
            }
            Hardware.infCycle = false
            Hardware.intCycle = 0
        } else if (Hardware.intCycle > 0) {
            while (Hardware.intCycle > 0) {
                oprt.run()
                Hardware.intCycle--
            }
        } else {
            println("This is not supposed to show. Main: CODE 1")
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        var input: String
        println("Enter exit to exit the program.")

        if(manual) {
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
                                        println("Running from: ${Thread.currentThread().name}")
                                        CLI.argIn(input)
                                        mainLoop()
                                        is1 = false
                                }
                                }
                            } else -> {
                                coroutineScope {
                                    launch(Dispatchers.Default) {
                                        println("Running from: ${Thread.currentThread().name}")
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
            val file = File("/Users/yaxar/Documents/Development/Projects/OSUI/app/src/main/java/com/yashar/osui/programs/job.txt")
            val input = Scanner(file)
            val list = ArrayList<String>()

            while (input.hasNextLine()) {
                list.add(input.nextLine())
            }

            do {
                CLI.argIn(list.removeAt(0))
                mainLoop()
            } while (!list.isEmpty())

            CLI.argIn("proc")
        }
    }

    fun GUI() = runBlocking {
        GlobalScope.launch(Dispatchers.Default) {
            do {
                println("Running from: ${Thread.currentThread().name}")
                mainLoop()
            } while (true)
        }
    }

    fun GUICreateJob(arg: Int) {
        JobCreator.makeProgram(arg)
    }
}