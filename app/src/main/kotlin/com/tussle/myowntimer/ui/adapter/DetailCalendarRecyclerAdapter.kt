package com.tussle.myowntimer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.DetailCalendarTodoItemBinding
import com.tussle.myowntimer.model.CalendarTodo

class DetailCalendarRecyclerAdapter(val data : MutableList<CalendarTodo>, context : Context) : RecyclerView.Adapter<DetailCalendarRecyclerAdapter.ToDoViewHolder>(){
    val mContext = context
    inner class ToDoViewHolder(private val binding:DetailCalendarTodoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(date : CalendarTodo){
            binding.calendarTodo.text = date.todo
            if(date.success)
                binding.calendarTodo.setTextColor(mContext.getColor(R.color.successColor))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding : DetailCalendarTodoItemBinding = DetailCalendarTodoItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.setting(data[position])
    }

    override fun getItemCount(): Int =data.size
}