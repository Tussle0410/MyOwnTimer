package com.tussle.myowntimer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.event.Event
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DetailNaviMenu
import com.tussle.myowntimer.model.Todo
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailViewModel(private val repo : Repo) : ViewModel() {
    private val _detailFragment = MutableLiveData(DetailNaviMenu.Timer)
    private val _countUpButtonEvent = MutableLiveData<Event<Boolean>>()
    private val _countDownButtonEvent = MutableLiveData<Event<Boolean>>()
    private val _countUpEvent = MutableLiveData<Boolean>()
    private val _countDownEvent = MutableLiveData<Boolean>()
    private val _insertTodoEvent = MutableLiveData<Event<Boolean>>()
    private val _getTodoEvent = MutableLiveData<Event<Boolean>>()
    private var countUpCheck : Boolean = false
    private var countDownCheck : Boolean = false
    var title : String
    var todoInfo = MutableLiveData<MutableList<Todo>>()
    val detailFragment : LiveData<DetailNaviMenu>
        get() = _detailFragment
    val countUpButtonEvent : LiveData<Event<Boolean>>
        get() = _countUpButtonEvent
    val countDownButtonEvent : LiveData<Event<Boolean>>
        get() = _countDownButtonEvent
    val countUpEvent : LiveData<Boolean>
        get() = _countUpEvent
    val countDownEvent : LiveData<Boolean>
        get() = _countDownEvent
    val insertTodoEvent : LiveData<Event<Boolean>>
        get() = _insertTodoEvent
    val getTodoEvent : LiveData<Event<Boolean>>
        get() = _getTodoEvent
    //Date And Time Info Set
    var countUpPauseTime : Long = 0L
    var countDownPauseTime : Long = 0L
    var countDownTime : Long = 0L
    var year : String
    var month : String
    var day : String
    var dayOfWeek : String
    init {
        _countUpButtonEvent.value = Event(true)
        with(GlobalApplication.prefs){
            title = titleGetString("title","")
            year = timeGetString("year","")
            month = timeGetString("month","")
            day = timeGetString("day","")
            dayOfWeek = timeGetString("dayOfWeek","") + "요일"
        }
    }
    //Detail Page Bottom Menu Click Listener
    val bottomMenuClickListener =
        NavigationBarView.OnItemSelectedListener { item ->
            val fragment = detailGetFragment(item.itemId)
            detailFragmentChange(fragment)
            true
        }
    //Get Current DetailPage Fragment
    private fun detailGetFragment(menu_id : Int) : DetailNaviMenu
        =when(menu_id){
            R.id.navi_timer -> DetailNaviMenu.Timer
            R.id.navi_todo -> DetailNaviMenu.Todo
            R.id.navi_calendar -> DetailNaviMenu.Calendar
            R.id.navi_chart -> DetailNaviMenu.Chart
            else -> throw IllegalArgumentException("Not Found Menu ID")
        }
    //DetailPage Fragment Change
    private fun detailFragmentChange(fragment : DetailNaviMenu){
        if(_detailFragment.value != fragment)
            _detailFragment.value = fragment
    }
    //TimerFragment StopWatch Button Click Method
    fun countUpButtonClick(){
        if(_countUpButtonEvent.value != Event(true)){
            _countUpButtonEvent.value = Event(true)
            _countDownButtonEvent.value = Event(false)
        }
    }
    //TimerFragment Timer Button Click Method
    fun countDownButtonClick(){
        if(_countDownButtonEvent.value != Event(true)){
            _countUpButtonEvent.value = Event(false)
            _countDownButtonEvent.value = Event(true)
        }
    }
    //getTodoInfo Method
    fun getTodoInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.getTodo(title).let {
                todoInfo.postValue(it)
            }
            _getTodoEvent.postValue(Event(true))
        }
    }
    //todoInsert Method
    fun insertTodo(todo_txt : String, success : Boolean){
        val todo = Todo(title, todo_txt, success)
        CoroutineScope(Dispatchers.IO).launch {
            repo.todoInsert(title, todo_txt, success)
        }
        addTodo(todo)
    }
    //Add todoInfo List
    private fun addTodo(todo : Todo){
        todoInfo.value!!.add(todo)
        _insertTodoEvent.value = Event(true)
    }
    //CountUp Start
    fun countUpStart(){
        _countUpEvent.value = countUpCheck
        countUpCheck = countUpCheck.not()
    }
    //CountDown Start
    fun countDownStart(){
        if(countDownTime!=0L){
            _countDownEvent.value = countDownCheck
            countDownCheck = countDownCheck.not()
        }
    }
    //CountDown End
    fun countDownEnd(){
        countDownTime = 0L
        _countDownEvent.value = countDownCheck
        countDownCheck = countDownCheck.not()
    }
}