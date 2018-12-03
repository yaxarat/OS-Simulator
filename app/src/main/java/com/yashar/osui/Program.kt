import java.util.ArrayList
import java.util.concurrent.ThreadLocalRandom

class Program(val name: String, commandIn: ArrayList<String>, val size: Int) {
    var runTime = 0
    var ranTime = 0
        private set
    var totalRanTime = 0
        private set
    var state: State? = null
    var pid: Int = 0
        private set
    val argumentQueue = ArrayList<String>()

    init {
        this.state = State.NEW
        this.argumentQueue.addAll(commandIn)
        for (anArgumentQueue in argumentQueue) {
            if (anArgumentQueue.contains("CALCULATE")) {
                val splitArgument = anArgumentQueue.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                totalRanTime = totalRanTime + Integer.valueOf(splitArgument[1])
            }
        }
    }

    fun advanceRunTime() {
        runTime--
        this.ranTime++
    }

    fun setArrivalTime(time: Int) {
        this.pid = time + ThreadLocalRandom.current().nextInt(1, 1000)
    }
}