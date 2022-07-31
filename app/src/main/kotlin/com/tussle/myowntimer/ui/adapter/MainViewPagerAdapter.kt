package com.tussle.myowntimer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.model.ViewPagerModel
import kotlinx.android.synthetic.main.main_viewpager_item.view.*

class MainViewPagerAdapter(val data : MutableList<ViewPagerModel>) : RecyclerView.Adapter<MainViewPagerAdapter.PageViewHolder>() {
    inner class PageViewHolder(parent : ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.main_viewpager_item,parent,false)
    ){
        var goalTodo : TextView = itemView.goalTodo
        var allTodo : TextView = itemView.allTodo
        var todo1 : TextView = itemView.main_viewpager_Todo1
        var todo2 : TextView = itemView.main_viewpager_Todo2
        var todo3 : TextView = itemView.main_viewpager_Todo3
        var timeToday : TextView = itemView.time_today
        var timeWeekend : TextView = itemView.time_weekend
        var timeMonth : TextView = itemView.time_month
        var timeTotal : TextView = itemView.time_total
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder
        =PageViewHolder(parent)

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        data[position].let { item ->
            with(holder){
                goalTodo.text = item.goalTodo
                allTodo.text = item.allTodo
                todo1.text = item.todo1
                todo2.text = item.todo2
                todo3.text = item.todo3
                timeToday.text = item.todayTime
                timeWeekend.text = item.weekendTime
                timeMonth.text = item.monthTime
                timeTotal.text = item.totalTime
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}