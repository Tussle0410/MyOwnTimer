package com.tussle.myowntimer.model.DB

import com.tussle.myowntimer.model.Title
import com.tussle.myowntimer.model.Todo
import com.tussle.myowntimer.sharedPreference.GlobalApplication


class Repo{
    private val dbInstance = GlobalApplication.databaseInstance.dao()

    suspend fun insert(title: Title){
        dbInstance.titleAdd(title)
    }
    suspend fun getAll() = dbInstance.getAll()
    suspend fun getInfo() = dbInstance.getInfo()
    suspend fun todoInsert(todo : Todo){
        dbInstance.todoAdd(todo)
    }
    suspend fun deleteAll(){
        dbInstance.deleteAll()
    }
}