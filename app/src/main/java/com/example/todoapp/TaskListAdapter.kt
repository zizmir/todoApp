package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TaskListAdapter(private val mTaskList:LinkedList<Task>): RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val mInflater = LayoutInflater.from(parent.context);
        val itemView = mInflater.inflate(R.layout.tasklist_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mTaskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(mTaskList[position])
    }

    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
         private var mTaskView: TextView? = null
         init {
             mTaskView = itemView.findViewById(R.id.task)

         }
         fun bind(task : Task){ mTaskView?.text = task.content}

     }
}