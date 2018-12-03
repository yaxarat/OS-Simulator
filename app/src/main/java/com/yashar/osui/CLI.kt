import java.io.File
import java.util.ArrayList
import java.util.Scanner

object CLI {
    private val oprt = Operator()
    private val queue = ArrayList<String>()

    fun argIn(input: String) {
        val argument = input.toLowerCase()

        if (argument == "proc") {
            proc(-1)
        } else if (argument == "exit") {
            System.exit(0)
        } else if (argument.contains("load")) {
            val splitArgument = argument.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (splitArgument.size == 2) {
                load(splitArgument[1])
            } else {
                println("Specify the program name: (ex: load browser)")
            }
        } else if (argument.contains("exe")) {
            val splitArgument = argument.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            when {
                splitArgument.size == 2 -> {
                    Hardware.infCycle = false
                    Hardware.intCycle = Integer.valueOf(splitArgument[1]) - 1
                    oprt.run()
                }
                splitArgument.size == 1 -> {
                    Hardware.infCycle = true
                    Hardware.intCycle = -1
                    oprt.run()
                }
                else -> println("Specify the execution cycle count: (ex: exe 20)")
            }
        } else {
            println("Not a valid command.")
        }
    }

    fun proc(pid: Int) {
        val newQueue = ProgramHandler.newQueue
        val readyQueue = ProgramHandler.readyQueue
        val waitQueue = ProgramHandler.waitQueue
        val exitQueue = ProgramHandler.exitQueue
        val margin = Formatter.margin

        println(Formatter.divider)
        println("Memory: " + (Hardware.usedMemory).toString() + "Mb used out of 2048Mb. " + (Hardware.getAvailableMemory().toString() + "Mb remaining.\n"))

        if (readyQueue.isEmpty()) {
            println("No programs are currently running.")
        } else {
            println("Running programs")
            if (pid == -1) {
                for (program in readyQueue) {
                    println(Formatter.ANSI_GREEN + "Program: " + program.name + margin + "State: " + program.state + margin + "Ran: " + program.ranTime + margin + "Remaining Runtime: " + Utility.rantimeManagement((program.totalRanTime - program.ranTime)) + margin + "Pid: " + program.pid + Formatter.ANSI_RESET)
                }
            } else {
                for (program in readyQueue) {
                    if (program.pid == pid) {
                        println(Formatter.ANSI_CYAN + "Program: " + program.name + margin + "State: " + program.state + margin + "Ran: " + program.ranTime + margin + "Remaining Runtime: " + Utility.rantimeManagement((program.totalRanTime - program.ranTime)) + margin + "Pid: " + program.pid + Formatter.ANSI_RESET)
                    } else {
                        println(Formatter.ANSI_GREEN + "Program: " + program.name + margin + "State: " + program.state + margin + "Ran: " + program.ranTime + margin + "Remaining Runtime: " + Utility.rantimeManagement((program.totalRanTime - program.ranTime)) + margin + "Pid: " + program.pid + Formatter.ANSI_RESET)
                    }
                }
            }
        }
        if (newQueue.isEmpty()) {
            println("No programs are currently waiting.")
        } else {
            println("Waiting programs")
            for (program in newQueue) {
                println(Formatter.ANSI_YELLOW + "Program: " + program.name + margin + "State: " + program.state + margin + "Ran: " + program.ranTime + margin + "Remaining Runtime: " + Utility.rantimeManagement((program.totalRanTime - program.ranTime)) + margin + "Pid: " + program.pid + Formatter.ANSI_RESET)
            }
        }
        if (exitQueue.isEmpty()) {
            println("No programs have finished.")
        } else {
            println("Finished programs")
            for (program in exitQueue) {
                println(Formatter.ANSI_RED + "Program: " + program.name + margin + "State: " + program.state + margin + "Ran: " + program.ranTime + margin + "Remaining Runtime: " + Utility.rantimeManagement((program.totalRanTime - program.ranTime)) + margin + "Pid: " + program.pid + Formatter.ANSI_RESET)
            }
        }
        println(Formatter.divider)
    }

    private fun load(argument: String) {
        parseFile(argument)
        if (!queue.isEmpty()) {
            val programSize = Integer.valueOf(queue.removeAt(0))
            ProgramHandler.insertProgram(Program(argument, queue, programSize))
        }
    }

    fun autoLoad(name: String, queue: ArrayList<String>) {
        if (!queue.isEmpty()) {
            val programSize = Integer.valueOf(queue.removeAt(0))
            ProgramHandler.insertProgram(Program(name, queue, programSize))
        }
    }

    private fun parseFile(filename: String) {
        queue.clear()
        val infile = "/Users/yaxar/Documents/Development/Projects/OSUI/app/src/main/java/com/yashar/osui/$filename.txt"
        println(infile)

        try {
            val file = File(infile)
            val scanner = Scanner(file)
            while (scanner.hasNextLine()) {
                queue.add(scanner.nextLine())
            }
            scanner.close()
        } catch (e: Exception) {
            println("No such program.")
        }

    }
}