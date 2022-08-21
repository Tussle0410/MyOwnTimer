package com.tussle.myowntimer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.myowntimer.event.Event
import com.tussle.myowntimer.model.*
import com.tussle.myowntimer.model.DB.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repo : Repo) : ViewModel(){
    private val _insertEvent = MutableLiveData<Event<Boolean>>()
    val insertEvent : LiveData<Event<Boolean>>
        get() = _insertEvent
    val viewPagerInfo = MutableLiveData<MutableList<ViewPagerModel>>()
    val titleInfo = MutableLiveData<List<TitleAndTodo>>()
    //DB Title Info Insert And MutableList Add
    fun insertTitle(title : String){
        CoroutineScope(Dispatchers.IO).launch {
            repo.titleInsert(Title(title))
        }
        addTitle(title)
    }
    //Get DB TitleAndTodo Table Info
    fun getTitle(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.getInfo().let {
                titleInfo.postValue(it)
            }
        }
    }
    //MutableList Title Info Add
    private fun addTitle(title : String){
        viewPagerInfo.value!!.add(ViewPagerModel(title,"0","0", mutableListOf(), mutableListOf(),
        "0H","0H","0H","0H"))
        _insertEvent.value = Event(true)
    }
    //TitleAndTodo Info -> ViewPagerModel info
    fun setList(){
        viewPagerInfo.value = mutableListOf()
        var index = -1
        var curTitle = ""
        for(cur in titleInfo.value!!){
            var check = false
            if(cur.title.title!=curTitle){
                check = true
                curTitle = cur.title.title
                index+=1
            }
            if(cur.todo==null){
                viewPagerInfo.value!!.add(ViewPagerModel(
                    cur.title.title,"0","0",
                    mutableListOf("", "", ""), mutableListOf(false,false,false),
                    cur.title.todayTime.toString(),cur.title.weekendTime.toString(),
                    cur.title.monthTime.toString(),cur.title.totalTime.toString()))
            }else{
                if(check){
                    viewPagerInfo.value!!.add(ViewPagerModel(
                        cur.title.title,if(cur.todo.success == true) "1" else "0","1",
                        mutableListOf(cur.todo.todo), mutableListOf(cur.todo.success),
                        cur.title.todayTime.toString(),cur.title.weekendTime.toString(),
                        cur.title.monthTime.toString(),cur.title.totalTime.toString()))
                }else{
                    with(viewPagerInfo.value!![index]){
                        todo.add(cur.todo.todo)
                        todoSuccess.add(cur.todo.success)
                        allTodo = (allTodo.toInt()+1).toString()
                        if(cur.todo.success==true)
                            goalTodo = (goalTodo.toInt()+1).toString()
                    }
                }
            }
        }
    }
}