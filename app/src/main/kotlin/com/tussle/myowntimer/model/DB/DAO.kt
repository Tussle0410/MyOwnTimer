package com.tussle.myowntimer.model.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tussle.myowntimer.model.*

@Dao
interface DAO {
    //Room DB Insert SQL
    @Insert
    suspend fun titleAdd(title: Title)

    @Query("INSERT INTO Todo(todo_title, todo, success) VALUES(:title, :todo, :success)")
    suspend fun todoAdd(title : String,todo : String, success:Boolean)

    @Query("INSERT INTO CalendarTime(calendar_title,time_date) VALUES(:title,:date)")
    suspend fun calendarTimeAdd(title : String, date : String)

    @Query("INSERT INTO CalendarTodo(calendar_title, todo_date, todo, success) VALUES(:title, :date, :todo, :success)")
    suspend fun calendarTodoAdd(title : String, date : String, todo : String, success: Boolean)
    //Room DB Update SQL
    @Query("UPDATE Todo SET success = :success WHERE todo = :todo AND todo_title = :title")
    suspend fun todoSuccessUpdate(title : String, todo : String, success: Boolean)

    @Query("UPDATE CalendarTodo SET success = :success Where todo = :todo AND todo_date = :date AND calendar_title = :title")
    suspend fun calendarTodoSuccessUpdate(title: String, todo: String, success: Boolean, date: String)

    @Query("UPDATE CalendarTime SET time = time + :time Where calendar_title = :title AND time_date = :date")
    suspend fun timeUpdate(time : Long, title : String, date: String)

    @Query("UPDATE Title SET todayTime = 0")
    suspend fun todayTimeInit()

    @Query("UPDATE Title SET todayTime = 0 AND monthTime = 0")
    suspend fun monthTimeInit()

    @Query("UPDATE Title SET todayTime = todayTime + :time, monthTime = monthTime + :time, totalTime = totalTime + :time WHERE title = :title")
    suspend fun titleTimeUpdate(time : Long, title : String)

    //Room DB SELECT SQL
    @Query("SELECT COUNT(title) FROM Title")
    suspend fun getTitleCount() : Int

    @Query("SELECT * FROM Title LEFT OUTER JOIN Todo ON Title.title == Todo.todo_title")
    suspend fun getInfo() : List<TitleAndTodo>?

    @Query("SELECT * FROM Todo WHERE todo_title == :title")
    suspend fun getTodo(title : String) :MutableList<Todo>

    @Query("SELECT * FROM CalendarTime WHERE calendar_title = :title AND time_date = :date")
    suspend fun getCalendarTime(title: String, date: String) : MutableList<CalendarTime>

    @Query("SELECT * FROM CalendarTime WHERE calendar_title = :title")
    suspend fun getAllCalendarTime(title:String) : MutableList<CalendarTime>

    @Query("SELECT * FROM CalendarTodo WHERE calendar_title = :title AND todo_date = :date")
    suspend fun getCalendarTodo(title : String, date : String) : MutableList<CalendarTodo>
    //Room DB DELETE SQL
}

