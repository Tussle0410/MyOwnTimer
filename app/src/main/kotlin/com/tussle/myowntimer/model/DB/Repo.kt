package com.tussle.myowntimer.model.DB

import com.tussle.myowntimer.model.Title
import com.tussle.myowntimer.model.Todo
import com.tussle.myowntimer.sharedPreference.GlobalApplication


class Repo{
    private val dbInstance = GlobalApplication.databaseInstance.dao()

    suspend fun titleInsert(title: Title){
        dbInstance.titleAdd(title)
    }
    suspend fun todoInsert(title : String, todo : String, success : Boolean ){
        dbInstance.todoAdd(title,todo,success)
    }
    suspend fun calendarTimeInsert(title : String, date : String){
        dbInstance.calendarTimeAdd(title, date)
    }
    suspend fun calendarTodoInsert(title:String, date : String, todo : String, success: Boolean){
        dbInstance.calendarTodoAdd(title, date, todo, success)
    }
    suspend fun todoSuccessUpdate(title : String, todo : String, success: Boolean){
        dbInstance.todoSuccessUpdate(title, todo, success)
    }
    suspend fun calendarTodoSuccessUpdate(title : String, todo: String, success: Boolean, date: String){
        dbInstance.calendarTodoSuccessUpdate(title, todo, success, date)
    }
    suspend fun timeUpdate(time : Long, title : String, date : String){
        dbInstance.timeUpdate(time, title, date)
    }
    suspend fun todayTimeInit(){
        dbInstance.todayTimeInit()
    }
    suspend fun monthTimeInit(){
        dbInstance.monthTimeInit()
    }
    suspend fun titleTimeUpdate(time:Long, title:String){
        dbInstance.titleTimeUpdate(time, title)
    }
    suspend fun getInfo() = dbInstance.getInfo()
    suspend fun getTitleCount() = dbInstance.getTitleCount()
    suspend fun getTodo(title:String)=dbInstance.getTodo(title)
    suspend fun getCalendarTime(title : String, date:String)=dbInstance.getCalendarTime(title, date)
    suspend fun getCalendarTodo(title : String, date : String) = dbInstance.getCalendarTodo(title, date)
    suspend fun getAllCalendarTime(title:String) = dbInstance.getAllCalendarTime(title)

}