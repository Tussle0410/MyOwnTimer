package com.tussle.myowntimer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.databinding.DetailTodoItemBinding

class DetailTodoRecyclerAdapter(val data : MutableList<String>) : RecyclerView.Adapter<DetailTodoRecyclerAdapter.ToDoViewHolder>(){
    inner class ToDoViewHolder(private val binding : DetailTodoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(todo : String){
            binding.todoTxt.text = todo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding : DetailTodoItemBinding = DetailTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.setting(data[position])
    }

    override fun getItemCount() = data.size
}