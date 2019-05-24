package com.example.todoapp.network

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.Task
import java.util.*

class TaskListAdapter(private val mTaskList:LinkedList<Task>, val onClickDelete: (Int)-> Unit, val onClickClose:(Int) -> Unit, val onClickReopen:(Int) -> Unit): RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

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
             itemView.findViewById<ImageButton>(R.id.trash).setOnClickListener {
                 onClickDelete(adapterPosition)
             }
             itemView.findViewById<CheckBox>(R.id.close).setOnCheckedChangeListener { buttonView, isChecked ->
                 if(isChecked){
                     onClickClose(adapterPosition)

                 }else{
                     onClickReopen(adapterPosition)
                 }
                    mTaskView?.strikeThrough = isChecked
             }

         }
         fun bind(task : Task){ mTaskView?.text = task.content}

        private var TextView.strikeThrough
            get() = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG > 0
            set(value) {
                paintFlags = if (value)
                    paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                else
                    paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

     }
}