package com.tussle.myowntimer.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.databinding.MainViewpagerItemBinding
import com.tussle.myowntimer.model.ViewPagerModel
import com.tussle.myowntimer.ui.activity.DetailActivity

class MainViewPagerAdapter(val data : MutableList<ViewPagerModel>) : RecyclerView.Adapter<MainViewPagerAdapter.PageViewHolder>() {
    inner class PageViewHolder(private val binding : MainViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(item : ViewPagerModel){
            binding.title.text = item.title
            binding.goalTodo.text = item.goalTodo
            binding.allTodo.text = item.allTodo
            binding.mainViewpagerTodo1.text = item.todo1
            binding.mainViewpagerTodo2.text = item.todo2
            binding.mainViewpagerTodo3.text = item.todo3
            binding.timeToday.text = item.todayTime
            binding.timeWeekend.text = item.weekendTime
            binding.timeMonth.text = item.monthTime
            binding.timeTotal.text = item.totalTime
            binding.viewPager.setOnClickListener { view ->
                val intent = Intent(view.context, DetailActivity::class.java)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder{
        val binding : MainViewpagerItemBinding = MainViewpagerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.setting(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

}