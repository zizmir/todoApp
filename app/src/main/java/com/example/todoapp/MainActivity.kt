package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.network.TaskListAdapter
import com.example.todoapp.network.TodoistApi

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton
import java.util.*

class MainActivity : AppCompatActivity() {

    private val mTaskList = LinkedList<Task>()

    fun getTasksList() {
        lifecycleScope.launch {
            val newTasks = TodoistApi.retrofitService.getTasks().await()
            mTaskList.clear()
            mTaskList.addAll(newTasks)
            recyclerview.adapter?.notifyDataSetChanged()
        }
    }

    private fun showAddItemDialog(onFinish: (String?) -> Unit) {
        val editText = EditText(this)
        alert("What do you want to do next?", "Add a new task") {
            customView = editText
            okButton { onFinish(editText.text.toString()) }
            cancelButton { onFinish(null) }
            onCancelled { onFinish(null) }
        }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recyclerview.adapter = TaskListAdapter(
            mTaskList,
            this::onDeleteItem,
            this::onCloseItem,
            this::onReopenItem
        )
        recyclerview.layoutManager = LinearLayoutManager(this)

        getTasksList()

        fab.setOnClickListener {
            showAddItemDialog { text ->
                if (text != null) {
                    lifecycleScope.launch {
                        val newTask = TodoistApi.retrofitService.createTasks(Task("", text)).await()
                        if (newTask != null) {
                            mTaskList.add(0, newTask)
                            recyclerview.adapter?.notifyItemInserted(0)
                            recyclerview.smoothScrollToPosition(0)
                        }
                    }
                }

            }

        }

    }

    private fun onReopenItem(position: Int){
        lifecycleScope.launch {
            val task = mTaskList[position]
            val res = TodoistApi.retrofitService.reopenTasks(task.id).await()
        }
    }

    private fun onDeleteItem(position: Int) {
        lifecycleScope.launch {
            val task = mTaskList[position]
            val res = TodoistApi.retrofitService.deleteTasks(task.id).await()
            if (res.isSuccessful) {
                mTaskList.remove(task)
                recyclerview.adapter?.notifyItemRemoved(position)
            }
        }

    }

    private fun onCloseItem(position: Int) {
        lifecycleScope.launch {
            val task = mTaskList[position]

            TodoistApi.retrofitService.closeTasks(task.id).await()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            R.id.action_refresh -> {
                getTasksList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
