package com.tussle.myowntimer.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.databinding.MainViewpagerItemBinding
import com.tussle.myowntimer.model.ViewPagerModel
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import com.tussle.myowntimer.ui.activity.DetailActivity
import com.tussle.myowntimer.ui.listener.CheckCalendarTime
import com.tussle.myowntimer.ui.listener.TitleUpdate

class MainViewPagerAdapter(val data : MutableList<ViewPagerModel>, context : Context, timeListener : CheckCalendarTime,
                updateListener : TitleUpdate) : RecyclerView.Adapter<MainViewPagerAdapter.PageViewHolder>() {
    val successColor = context.getColor(R.color.successColor)
    val timeCallBack = timeListener
    val updateCallBack = updateListener
    inner class PageViewHolder(private val binding : MainViewpagerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun setting(item : ViewPagerModel){
            binding.title.text = item.title
            binding.goalTodo.text = item.goalTodo
            binding.allTodo.text = item.allTodo
            when(item.todo.size){
                0 -> {}
                1 -> {
                    binding.mainViewpagerTodo1.text = item.todo[0]
                    if (item.todoSuccess[0] == true) setTodo1TextColor()
                }
                2 -> {
                    binding.mainViewpagerTodo1.text = item.todo[0]
                    if (item.todoSuccess[0] == true) setTodo1TextColor()
                    binding.mainViewpagerTodo2.text = item.todo[1]
                    if (item.todoSuccess[1] == true) setTodo2TextColor()
                }
                else -> {
                    binding.mainViewpagerTodo1.text = item.todo[0]
                    if (item.todoSuccess[0] == true) setTodo1TextColor()
                    binding.mainViewpagerTodo2.text = item.todo[1]
                    if (item.todoSuccess[1] == true) setTodo2TextColor()
                    binding.mainViewpagerTodo3.text = item.todo[2]
                    if (item.todoSuccess[2] == true) setTodo3TextColor()
                }
            }
            binding.timeToday.text = timeConverter(item.todayTime)
            binding.timeMonth.text = timeConverter(item.monthTime)
            binding.timeTotal.text = timeConverter(item.totalTime)
            binding.viewPager.setOnClickListener { view ->
                GlobalApplication.prefs.titleSetString("title", item.title)
                timeCallBack.checkCalendarTime(item.title)
                val intent = Intent(view.context, DetailActivity::class.java)
                view.context.startActivity(intent)
            }
            binding.titleUpdate.setOnClickListener {
                updateCallBack.titleUpdate(item.title)
            }
        }
        private fun setTodo1TextColor(){
                binding.mainViewpagerTodo1.setTextColor(successColor)
        }
        private fun setTodo2TextColor(){
                binding.mainViewpagerTodo2.setTextColor(successColor)
        }
        private fun setTodo3TextColor(){
                binding.mainViewpagerTodo3.setTextColor(successColor)
        }
        private fun timeConverter(time : Int) : String{
            val h = time/3600
            val m = (time/60)%60
            val s = time%60
            val txtH = if(h<10) "0$h" else h.toString()
            val txtM = if(m<10) "0$m" else m.toString()
            val txtS = if(s<10) "0$s" else s.toString()
            return "$txtH:$txtM:$txtS"
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