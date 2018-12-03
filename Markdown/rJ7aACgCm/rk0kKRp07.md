# Loading external and generating new processes
Process or prgram and job files are generated through below python script per user demand.
```python
from random import randint

number = input("Enter amount of programs you want to create: ")
programs = []
i = 0
n = 0


while i < int(number):
    path = "program{}.txt".format(i)
    name = "program{}".format(i)
    command_len = randint(10, 40)
    programs.append(name)
    file = open(path, "w+")
    file.write("{}\n".format(str(randint(50, 800))))

    while command_len > 0:
        option = randint(0, 5)

        if option == 0:
            file.write("CALCULATE {}\n".format(str(randint(100, 500))))
        elif option == 1:
            file.write("IO\n")
        elif option == 2:
            file.write("YIELD\n")
        elif option == 3:
            file.write("CS\n")
        elif option == 4:
            file.write("CE\n")
        else:
            file.write("OUT\n")

        command_len -= 1

    file.close()
    i += 1

with open('job.txt', 'w') as file_handle:
    for list_item in programs:
        file_handle.write("LOAD {}\n".format(list_item))
    file_handle.write("EXE")
```
Or, when using GUI, through the below Kotlin JobCreator object:
```kotlin
fun makeProgram(numProcess: Int) {
        var count = 0

        while (count < numProcess) {
            var commandLen = 0
            val commandLimit = randPicker(6, 20)
            val crit1 = randPicker(0, commandLimit / 2)
            val crit2 = randPicker(commandLimit / 2, commandLimit)

            while (commandLen < commandLimit) {
                val option = optionPicker()
                programTaskQueue.add(randPicker(450, 1700).toString())

                when (commandLen) {
                    crit1 -> {
                        programTaskQueue.add("CS")
                    }
                    crit2 -> {
                        programTaskQueue.add("CE")
                    }
                }

                when (option) {
                    0 -> {
                        programTaskQueue.add("CALCULATE ${randPicker(100, 350)}")
                    }
                    1 -> {
                        programTaskQueue.add("IO")
                    }
                    2 -> {
                        programTaskQueue.add("YIELD")
                    }
                    3 -> {
                        programTaskQueue.add("OUT")
                    }
                }
                commandLen++
            }
            println(programTaskQueue)
            CLI.autoLoad("Program$globalCount", programTaskQueue)
            programTaskQueue.clear()
            count++
            globalCount++
        }
    }
}
```
Which can then be loaded via below functions in the CLI class:
Load command verifier
```kotlin
if (argument.contains("load")) {
            val splitArgument = argument.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (splitArgument.size == 2) {
                load(splitArgument[1])
            } else {
                println("Specify the program name: (ex: load browser)")
            }
        } 
```
If load command is valid, it get's passed to the load command processor:
```kotlin
    private fun load(argument: String) {
        parseFile(argument)
        if (!queue.isEmpty()) {
            val programSize = Integer.valueOf(queue.removeAt(0))
            Scheduler.insertProgram(Program(argument, queue, programSize))
        }
    }
```
