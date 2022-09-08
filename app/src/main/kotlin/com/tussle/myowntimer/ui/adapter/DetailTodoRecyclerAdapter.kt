package com.tussle.myowntimer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.databinding.DetailTodoItemBinding
import com.tussle.myowntimer.model.Todo
import com.tussle.myowntimer.ui.listener.TodoSuccessUpdate
import com.tussle.myowntimer.ui.listener.TodoTextUpdate

class DetailTodoRecyclerAdapter(val data : MutableList<Todo>, successListener : TodoSuccessUpdate,
                        updateListener : TodoTextUpdate
): RecyclerView.Adapter<DetailTodoRecyclerAdapter.ToDoViewHolder>(){
    private val successCallBack = successListener
    private val updateCallBack = updateListener
    inner class ToDoViewHolder(private val binding : DetailTodoItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(todo : Todo){
            binding.todoTxt.text = todo.todo
            binding.todoCheckBox.isChecked = todo.success!!
            binding.todoCheckBox.setOnCheckedChangeListener { _, _ ->
                successCallBack.successUpdate(todo.todo!!, binding.todoCheckBox.isChecked)
            }
            binding.todoTxt.setOnLongClickListener {
                updateCallBack.todoTextUpdateListener(todo.todo!!)
                true
            }
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