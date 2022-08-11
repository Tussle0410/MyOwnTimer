package com.tussle.myowntimer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.databinding.DetailCalendarTodoItemBinding

class DetailCalendarRecyclerAdapter(val data : MutableList<String>) : RecyclerView.Adapter<DetailCalendarRecyclerAdapter.ToDoViewHolder>(){
    inner class ToDoViewHolder(private val binding:DetailCalendarTodoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(todo : String){
            binding.calendarTodo.text = todo
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