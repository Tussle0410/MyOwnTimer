package com.tussle.myowntimer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.myowntimer.event.Event
import com.tussle.myowntimer.model.*
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(private val repo : Repo) : ViewModel(){
    private val _insertEvent = MutableLiveData<Event<Boolean>>()
    private val _updateEvent = MutableLiveData<Event<Boolean>>()
    private val _deleteEvent = MutableLiveData<Event<Boolean>>()
    val insertEvent : LiveData<Event<Boolean>>
        get() = _insertEvent
    val updateEvent : LiveData<Event<Boolean>>
        get() = _updateEvent
    val deleteEvent : LiveData<Event<Boolean>>
        get() = _deleteEvent
    val viewPagerInfo = MutableLiveData<MutableList<ViewPagerModel>>()
    val titleInfo = MutableLiveData<List<TitleAndTodo>>()
    val continueDay = MutableLiveData<String>()
    var titleCount = GlobalApplication.prefs.titleGetInt("titleCount",0)
    private lateinit var date : String
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
        0,0,0))
        _insertEvent.value = Event(true)
        titleCount++
        GlobalApplication.prefs.titleSetInt("titleCount",titleCount)
    }
    //Room DB Title Table Update
    fun updateTitle(title : String, previousTitle : String){
        CoroutineScope(Dispatchers.IO).launch {
            repo.titleUpdate(title, previousTitle)
        }
        updateViewPagerInfo(title, previousTitle)
    }
    //ViewPagerInfo Title Update
    private fun updateViewPagerInfo(title : String, previousTitle: String){
        for( info in viewPagerInfo.value!!){
            if(info.title == previousTitle){
                info.title = title
                break
            }
        }
        _updateEvent.value = Event(true)
    }
    //Room DB Title Table Delete
    fun deleteTitle(title:String){
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteTitle(title)
        }
        deleteViewPagerInfo(title)
    }
    //ViewPagerInfo Title Update
    private fun deleteViewPagerInfo(title : String){
        for(i in 0..viewPagerInfo.value!!.size){
            if(viewPagerInfo.value!![i].title == title){
                viewPagerInfo.value!!.removeAt(i)
                break
            }
        }
        titleCount--
        _deleteEvent.value = Event(true)
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
                    cur.title.todayTime, cur.title.monthTime,cur.title.totalTime))
            }else{
                if(check){
                    viewPagerInfo.value!!.add(ViewPagerModel(
                        cur.title.title,if(cur.todo.success == true) "1" else "0","1",
                        mutableListOf(cur.todo.todo), mutableListOf(cur.todo.success),
                        cur.title.todayTime, cur.title.monthTime,cur.title.totalTime))
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
    //Title CalenderTime Check
    fun calendarTimeCheck(title : String){
        CoroutineScope(Dispatchers.IO).launch {
            if(repo.getCalendarTime(title, date).isEmpty()){
                repo.calendarTimeInsert(title, date)
                for (value in repo.getTodo(title)){
                    repo.calendarTodoInsert(title, date, value.todo!!, value.success!!)
                }
            }
        }
    }
    //Today Date Check Method
    fun setDate(){
        val format = SimpleDateFormat("yyyy-MM-dd 00:00:00", Locale.getDefault())
        date = format.format(Calendar.getInstance().time)
        val time = format.parse(date)!!.time
        val previousTime = GlobalApplication.prefs.timeGetString("date","1997-04-10 00:00:00")
        val dif = (time - format.parse(previousTime)!!.time) / (60*60*24*1000)
        continueDay.value = GlobalApplication.prefs.timeGetInt("continue",0).toString()
        if(dif>=1){
            if(dif==1L){
                val temp = continueDay.value!!.toInt()+1
                GlobalApplication.prefs.timeSetInt("continue",temp)
                continueDay.value = temp.toString()
            }else
                GlobalApplication.prefs.timeSetInt("continue",1)

            if(date.substring(5, 7) != previousTime.substring(5, 7)){
                CoroutineScope(Dispatchers.IO).launch {
                    repo.monthTimeInit()
                }
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    repo.todayTimeInit()
                }
            }
            GlobalApplication.prefs.timeSetString("date", date)
        }
    }
}