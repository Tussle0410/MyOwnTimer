package com.tussle.myowntimer.event

import androidx.lifecycle.Observer
//-------Event Open Class Observer------------
class EventObserver<T>(private val onEventUnhandledContent : (T)->Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.let { value ->
            onEventUnhandledContent(value)
        }
    }
}