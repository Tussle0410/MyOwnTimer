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

    @Insert
    suspend fun todoAdd(todo : Todo)

    @Query("SELECT COUNT(title) FROM Title")
    suspend fun getTitleCount() : Int

    @Query("SELECT * FROM Title LEFT OUTER JOIN Todo ON Title.title == Todo.todo_title")
    suspend fun getInfo() : List<TitleAndTodo>?

    @Query("SELECT * FROM Todo WHERE todo_title == :title")
    suspend fun getTodo(title : String) :List<Todo>
}

