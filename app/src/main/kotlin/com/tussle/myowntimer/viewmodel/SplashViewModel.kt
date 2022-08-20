package com.tussle.myowntimer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tussle.myowntimer.event.Event
import com.tussle.myowntimer.model.DB.Repo
import com.tussle.myowntimer.sharedPreference.GlobalApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SplashViewModel(private val repo : Repo) : ViewModel() {
    private val _splashEvent = MutableLiveData<Event<Boolean>>()
    val splashEvent : LiveData<Event<Boolean>>
        get() = _splashEvent
    //Splash Method
    fun splashStart(){
        GlobalScope.launch (Dispatchers.IO){
            setDate()
            GlobalApplication.prefs.titleSetInt("titleCount",repo.getTitleCount())
            delay(2000)
            _splashEvent.postValue(Event(true))
        }
    }
    //Today Date Set
    private fun setDate(){
        val date = Calendar.getInstance().time
        val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
        val month = SimpleDateFormat("LL", Locale.getDefault()).format(date)
        val day = SimpleDateFormat("dd", Locale.getDefault()).format(date)
        val dayOfWeek = SimpleDateFormat("EE", Locale.getDefault()).format(date)
        with(GlobalApplication.prefs){
            if(year != timeGetString("year","") || month != timeGetString("month","") ||
                    day != timeGetString("day","")){
                timeSetString("year",year)
                timeSetString("month",month)
                timeSetString("day",day)
                timeSetString("dayOfWeek",dayOfWeek)
                timeSetInt("time",0)
            }
        }
    }
}