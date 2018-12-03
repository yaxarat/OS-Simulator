package com.yashar.osui

import Program

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