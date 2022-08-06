package com.tussle.myowntimer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.navigation.NavigationBarView
import com.tussle.myowntimer.R
import com.tussle.myowntimer.event.Event
import com.tussle.myowntimer.model.DetailNaviMenu

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _detailFragment = MutableLiveData(DetailNaviMenu.Timer)
    private val _countUpButtonEvent = MutableLiveData<Event<Boolean>>()
    private val _countDownButtonEvent = MutableLiveData<Event<Boolean>>()
    val detailFragment : LiveData<DetailNaviMenu>
        get() = _detailFragment
    val countUpButtonEvent : LiveData<Event<Boolean>>
        get() = _countUpButtonEvent
    val countDownButtonEvent : LiveData<Event<Boolean>>
        get() = _countDownButtonEvent
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

}