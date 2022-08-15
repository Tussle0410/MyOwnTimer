package com.tussle.myowntimer.model.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tussle.myowntimer.model.Title
import com.tussle.myowntimer.model.Todo

@Database(entities = [Title::class, Todo::class], version = 2)
abstract class DB : RoomDatabase(){
    abstract fun dao() : DAO
}