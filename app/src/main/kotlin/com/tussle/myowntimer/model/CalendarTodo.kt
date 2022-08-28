package com.tussle.myowntimer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Title::class,
    parentColumns = ["title"],
    childColumns = ["calendar_title"],
    onDelete = CASCADE,
    onUpdate = CASCADE
)])
data class CalendarTodo(
    val calendar_title : String,
    val todo_date : String,
    val todo : String,
    val success : Boolean,
    @PrimaryKey(autoGenerate = true)
    val id : Int?
)
