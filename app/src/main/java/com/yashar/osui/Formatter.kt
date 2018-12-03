object Formatter {
    const val ANSI_RESET = "\u001B[0m"
    const val ANSI_RED = "\u001B[31m"
    const val ANSI_GREEN = "\u001B[32m"
    const val ANSI_YELLOW = "\u001B[33m"
    const val ANSI_CYAN = "\u001B[36m"
    const val ANSI_BLACK = "\u001B[30m"
    const val ANSI_WHITE_BACKGROUND = "\u001B[47m"

    var margin = "$ANSI_BLACK | $ANSI_RESET"
    var divider = "$ANSI_WHITE_BACKGROUND------------------------------------------------------------------------------------------$ANSI_RESET"
}
