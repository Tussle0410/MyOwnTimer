package com.tussle.myowntimer.model.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tussle.myowntimer.model.Title
import com.tussle.myowntimer.model.TitleAndTodo
import com.tussle.myowntimer.model.Todo

@Dao
interface DAO {
    @Insert
    suspend fun titleAdd(title: Title)

    @Query("DELETE FROM Title")
    suspend fun deleteAll()

    @Query("SELECT * FROM Title")
    suspend fun getAll() : List<Title>

    @Insert
    suspend fun todoAdd(todo : Todo)

    @Query("SELECT * FROM Title LEFT OUTER JOIN Todo ON Title.title == Todo.todo_title")
    suspend fun getInfo() : List<TitleAndTodo>?
}

