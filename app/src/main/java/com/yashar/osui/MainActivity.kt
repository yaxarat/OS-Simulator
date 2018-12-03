package com.yashar.osui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import android.widget.ArrayAdapter
import ProgramHandler

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addProcessBtn.setOnClickListener {
            if (numProgramEt.text.isNotEmpty()) {
                val arg = numProgramEt.text.toString().toInt()
                Main.GUI(arg)
            }
            updateUI()
        }
        procBtn.setOnClickListener {
            CLI.proc(-1)
            updateUI()
        }
        exeBtn.setOnClickListener {
            CLI.argIn("exe")
            updateUI()
        }
        exeNumBtn.setOnClickListener {
            if (numCycleEt.text.isNotEmpty()){
                val arg = numCycleEt.text.toString().toInt()
                CLI.argIn("exe $arg")
            }
            updateUI()
        }
        exitBtn.setOnClickListener {
            CLI.argIn("exit")
            updateUI()
        }

        updateWaitList()
    }

    fun updateUI() {
        Hardware.memoryCleaner()
        memEt.text = "Free Memory: ${Hardware.getAvailableMemory()}"
        runningList.adapter = null
        waitingList.adapter = null
        finishedList.adapter = null
        updateRunList()
        updateWaitList()
        updateFinishList()
    }

    private fun updateWaitList() {
        val list = ProgramHandler.newQueue
        val nameList = ArrayList<String>()
        val pidList = ArrayList<Int>()
        println("HERE $list")
        for (program in list) {
            nameList.add(program.name)
            //pidList.add(program.pid)
        }
        println(list.toString())
        waitListAdapter(nameList)
    }

    private fun updateRunList() {
        val list = ProgramHandler.readyQueue
        val nameList = ArrayList<String>()
        val pidList = ArrayList<Int>()
        println("HERE $list")
        for (program in list) {
            nameList.add(program.name)
            //pidList.add(program.pid)
        }
        println(list.toString())
        readyListAdapter(nameList)
    }

    private fun updateFinishList() {
        val list = ProgramHandler.exitQueue
        val nameList = ArrayList<String>()
        val pidList = ArrayList<Int>()
        println("HERE $list")
        for (program in list) {
            nameList.add(program.name)
            //pidList.add(program.pid)
        }
        println(list.toString())
        exitListAdapter(nameList)
    }

    private fun waitListAdapter(waitList: ArrayList<String>?) {
        // set up adapter for listView
        if (waitList != null) {
            var count = 0
            while (count < waitList.size) {
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
                adapter.addAll(waitList)
                waitingList!!.adapter = adapter
                count++
            }
        }
    }

    private fun readyListAdapter(readyList: ArrayList<String>?) {
        // set up adapter for listView
        if (readyList != null) {
            var count = 0
            while (count < readyList.size) {
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
                adapter.addAll(readyList)
                runningList!!.adapter = adapter
                count++
            }
        }
    }

    private fun exitListAdapter(exitList: ArrayList<String>?) {
        // set up adapter for listView
        if (exitList != null) {
            var count = 0
            while (count < exitList.size) {
                val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
                adapter.addAll(exitList)
                finishedList!!.adapter = adapter
                count++
            }
        }
    }
}
