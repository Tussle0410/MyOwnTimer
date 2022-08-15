package com.tussle.myowntimer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tussle.myowntimer.event.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private val _splashEvent = MutableLiveData<Event<Boolean>>()
    val splashEvent : LiveData<Event<Boolean>>
        get() = _splashEvent

    //Splash Method
    fun splashStart(){
        GlobalScope.launch (Dispatchers.IO){
            delay(2000)
            _splashEvent.postValue(Event(true))
        }
    }
}