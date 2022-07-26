package com.tussle.myowntimer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationBarView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.event.Event
import com.tussle.myowntimer.model.CalendarTime
import com.tussle.myowntimer.model.CalendarTodo
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.model.DetailNaviMenu
import com.tussle.myowntimer.model.Todo
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class DetailViewModel(private val repo : Repo) : ViewModel() {
    private val _detailFragment = MutableLiveData(DetailNaviMenu.Timer)
    private val _countUpButtonEvent = MutableLiveData<Event<Boolean>>()
    private val _countDownButtonEvent = MutableLiveData<Event<Boolean>>()
    private val _countUpEvent = MutableLiveData<Boolean>()
    private val _countDownEvent = MutableLiveData<Boolean>()
    private val _todoUpdateEvent = MutableLiveData<Event<Boolean>>()
    private val _todoDeleteEvent = MutableLiveData<Event<Boolean>>()
    private var countUpCheck : Boolean = false
    private var countDownCheck : Boolean = false
    val date = GlobalApplication.prefs.timeGetString("date","")
    var chartDate = MutableLiveData<String>()
    var title = MutableLiveData<String>()
    val countDownAlarm = GlobalApplication.prefs.settingGetString("alarm", "sound+vibrate")
    var todoInfo = MutableLiveData<MutableList<Todo>>()
    var calendarTimeInfo = MutableLiveData<MutableList<CalendarTime>>()
    var calendarTodoInfo = MutableLiveData<MutableList<CalendarTodo>>()
    var calendarTimeHash = MutableLiveData<HashMap<String,Int>>()
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
    val todoUpdateEvent : LiveData<Event<Boolean>>
        get() = _todoUpdateEvent
    val todoDeleteEvent : LiveData<Event<Boolean>>
        get() = _todoDeleteEvent
    //Date And Time Info Set
    var countUpPauseTime : Long = 0L
    var countDownPauseTime : Long = 0L
    var countDownTime : Long = 0L
    private var countUpSaveTime : Long = 0L
    private var countDownSaveTime : Long = 0L
    var chartMaxTime : Long = 0L
    var chartSumTime : Long = 0L
    var chartAvgTime : Long = 0L
    private var chartTimeCount : Int = 0
    lateinit var year : String
    lateinit var month : String
    lateinit var day : String
    lateinit var dayOfWeek : String
    var curCalendarDate : String = date
    init {
        _countUpButtonEvent.value = Event(true)
        title.value = GlobalApplication.prefs.titleGetString("title","")
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
            repo.getTodo(title.value!!).let {
                todoInfo.postValue(it)
            }
        }
    }
    //todoInsert Method
    fun insertTodo(todo_txt : String, success : Boolean){
        val todo = Todo(title.value!!, todo_txt, success)
        CoroutineScope(Dispatchers.IO).launch {
            repo.todoInsert(title.value!!, todo_txt, success)
            repo.calendarTodoInsert(title.value!!, date, todo_txt, success)
        }
        addTodo(todo)
    }
    //Add todoInfo List
    private fun addTodo(todo : Todo){
        todoInfo.value!!.add(todo)
    }
    //Update todo_Success
    fun todoSuccessUpdate(todo : String, success : Boolean){
        CoroutineScope(Dispatchers.IO).launch {
            repo.todoSuccessUpdate(title.value!!, todo, success)
            repo.calendarTodoSuccessUpdate(title.value!!, todo, success, date)
        }
    }
    //Update TodoText
    fun todoUpdate(todo : String, previousTodo : String){
        CoroutineScope(Dispatchers.IO).launch {
            repo.todoUpdate(title.value!!, todo, previousTodo)
            repo.calendarTodoUpdate(title.value!!, todo, date, previousTodo)
        }
        updateTodoList(todo, previousTodo)
    }
    //Update todoInfo List
    private fun updateTodoList(todo : String, previousTodo: String){
        for(cur in todoInfo.value!!){
            if(cur.todo == previousTodo){
                cur.todo = todo
                _todoUpdateEvent.value = Event(true)
                return
            }
        }
    }
    //DeleteTodo
    fun deleteTodo(todo : String){
        CoroutineScope(Dispatchers.IO).launch {
            repo.deleteTodo(todo, title.value!!)
            repo.deleteCalendarTodo(todo, title.value!!, date)
        }
        deleteTodoList(todo)
    }
    //Delete TodoList
    private fun deleteTodoList(todo : String){
        for(i in 0..todoInfo.value!!.size){
            if(todoInfo.value!![i].todo == todo){
                todoInfo.value!!.removeAt(i)
                _todoDeleteEvent.value = Event(true)
                return
            }
        }
    }

    //timeUpdate
    private fun calendarTimeUpdate(time : Long){
        CoroutineScope(Dispatchers.IO).launch {
            repo.timeUpdate(time, title.value!!, date)
        }
        with(calendarTimeHash.value!!){
            val tempTime = get(date)?.plus(time)
            put(date, tempTime!!.toInt())
        }
        titleTimeUpdate(time)
    }
    //CountUp Start
    fun countUpStart(){
        _countUpEvent.value = countUpCheck
        countUpCheck = countUpCheck.not()
    }
    //Time Update
    fun timeUpdate(check: Boolean) : Boolean{
        val time = if(check)
            countUpSaveTime/1000 - countUpPauseTime/1000
        else
            countDownSaveTime/1000 - countDownPauseTime/1000
        if(time>=3 && check){
            countUpSaveTime = countUpPauseTime
            calendarTimeUpdate(time)
            return true
        }else if(!check){
            countDownSaveTime = countDownPauseTime
            calendarTimeUpdate(time)
            return true
        }
        return false
    }
    //Init SaveTime
    fun saveTimeInit(check : Boolean){
        if(check)
            countUpSaveTime = 0L
        else
            countDownSaveTime = 0L
    }
    //CountDown Start
    fun countDownStart(){
        _countDownEvent.value = countDownCheck
        countDownCheck = countDownCheck.not()
    }
    //CountDown End
    fun countDownEnd(){
        countDownTime = 0L
        countDownPauseTime = 0L
        countDownStart()
    }
    //CountDownTimeSetting
    fun dialogInfoToCountDownTime(h : Int, m : Int, s : Int){
        countDownTime = (h*3600 + m*60 + s)*1000.toLong()
    }
    fun countDownPauseTimeSet(time : Long){
        countDownPauseTime = time
    }
    fun countDownSaveTimeSet(time : Long){
        countDownSaveTime = time
    }
    //Init CountDownPauseTime
    fun initCountDownPauseTime(){
        countDownPauseTime = 0L
    }
    //Set Current Date
    fun setDate(){
        val date = Calendar.getInstance().time
        year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
        month = SimpleDateFormat("MM", Locale.getDefault()).format(date)
        day = SimpleDateFormat("dd", Locale.getDefault()).format(date)
        dayOfWeek = SimpleDateFormat("EE", Locale.getDefault()).format(date) + "요일"
        chartDate.value = "$year-$month"
    }
    //Get CalendarTime Date
    fun getCalendarTime(){
        CoroutineScope(Dispatchers.IO).launch {
            repo.getAllCalendarTime(title.value!!).let {
                    calendarTimeInfo.postValue(it)
            }
        }
    }
    //Get CalendarTodo Date
    fun getCalendarTodo(date : String){
        CoroutineScope(Dispatchers.IO).launch {
            repo.getCalendarTodo(title.value!!, date).let {
                calendarTodoInfo.postValue(it)
            }
        }
    }
    //CalendarTime List -> HashMap
    fun calendarTimeListToHash(){
        calendarTimeHash.value = HashMap()
        for ( cur in calendarTimeInfo.value!!){
            calendarTimeHash.value!![cur.time_date] = cur.time
        }
    }
    //CalendarTime HashMap Empty Check
    fun calendarTimeEmpty(date : String) : Boolean{
        val tempTime = calendarTimeHash.value!!.getOrDefault(date,0)
        return tempTime>0
    }
    //Get CalendarTimeHash Info -> TimeConvert
    fun getCalendarTimeHashConvert(date : String) : String
        = timeConvert((calendarTimeHash.value?.getOrDefault(date,0))!!.toLong())

    //Get CalendarTimeHash Info -> Time
    fun getCalendarTimeHashTime(date : String) : Int?{
        return if(calendarTimeHash.value!!.containsKey(date)){
            chartTimeCount++
            calendarTimeHash.value!![date]
        }else
            0
    }

    //Compare ChartTimeMax
    fun compareChartTimeMax(time: Int){
        if(time > chartMaxTime)
            chartMaxTime = time.toLong()
    }
    //Sum ChartTime
    fun sumChartTime(time:Int){
        chartSumTime+=time.toLong()
    }
    //Minus ChartTime
    fun minusChartTime(){
        chartSumTime-= getCalendarTimeHashTime(date)!!
    }
    //Avg ChartTime
    fun avgCharTime(){
        if(chartTimeCount!=0)
            chartAvgTime =chartSumTime/chartTimeCount
    }
    //Init ChartTime
    fun initChartTime(){
        chartSumTime=0L
        chartAvgTime=0L
        chartMaxTime=0L
        chartTimeCount=0
    }
    //Reduce Month of ChartDate
    fun monthPreviousChange(){
        var tYear = chartDate.value!!.substring(0,4).toInt()
        var tMonth = chartDate.value!!.substring(5).toInt()
        if(tMonth== 1){
            tYear--
            chartDate.value = "$tYear-12"
        }else{
            tMonth--
            if(tMonth>9)
                chartDate.value = "$tYear-$tMonth"
            else
                chartDate.value = "$tYear-0$tMonth"
        }
    }
    //Next Month of ChartDate
    fun monthNextChange(){
        var tYear = chartDate.value!!.substring(0,4).toInt()
        var tMonth = chartDate.value!!.substring(5).toInt()
        if(tMonth==12){
            tYear++
            chartDate.value = "$tYear-01"
        }else{
            tMonth++
            if(tMonth>9)
                chartDate.value = "$tYear-$tMonth"
            else
                chartDate.value = "$tYear-0$tMonth"
        }
    }
    //Current Month of ChartDate equals TodayDate
    fun chartMonthCheck() : Boolean{
        return chartDate.value != date.substring(0,7)
    }
    //TitleTimeUpdate
    private fun titleTimeUpdate(time : Long){
        CoroutineScope(Dispatchers.IO).launch {
            repo.titleTimeUpdate(time, title.value!!)
        }
    }
    //curCalendarDate Value Change
    fun setCalendarDate(date : String){
        curCalendarDate = date
    }
    //Convert MillSec -> HH:MM:SS
    fun timeConvert(sec : Long) : String{
        val h = sec/3600
        val m = (sec/60)%60
        val s = sec%60
        val txtH = if(h<10) "0$h" else h.toString()
        val txtM = if(m<10) "0$m" else m.toString()
        val txtS = if(s<10) "0$s" else s.toString()
        return "$txtH:$txtM:$txtS"
    }
    //ProgressValue Cal
    fun progressValueCal(time : Long) : Int{
        return (((time/1000).toFloat() / (countDownTime/1000))*100).toInt()
    }
}