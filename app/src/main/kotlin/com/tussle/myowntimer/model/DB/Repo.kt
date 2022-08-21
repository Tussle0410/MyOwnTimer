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
    suspend fun getInfo() = dbInstance.getInfo()
    suspend fun getTitleCount() = dbInstance.getTitleCount()
    suspend fun getTodo(title:String)=dbInstance.getTodo(title)

}