package com.tussle.myowntimer.viewmodel

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

class SplashViewModel(private val repo : Repo) : ViewModel() {
    private val _splashEvent = MutableLiveData<Event<Boolean>>()
    val splashEvent : LiveData<Event<Boolean>>
        get() = _splashEvent
    //Splash Method
    fun splashStart(){
        GlobalScope.launch (Dispatchers.IO){
            GlobalApplication.prefs.titleSetInt("titleCount",repo.getTitleCount())
            delay(2000)
            _splashEvent.postValue(Event(true))
        }
    }
}