object Utility {

    fun rantimeManagement(rantime: Int): Int {
        return if (rantime >= 0) {
            rantime
        } else {
            0
        }
    }
}