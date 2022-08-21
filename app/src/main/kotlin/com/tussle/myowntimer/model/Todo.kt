package com.tussle.myowntimer.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(
    entity = Title::class,
    parentColumns = ["title"],
    childColumns = ["todo_title"],
    onDelete = CASCADE,
    onUpdate = CASCADE
)]
)
data class Todo(
    val todo_title : String?,
    val todo : String?,
    val success : Boolean? = false,
    @PrimaryKey(autoGenerate = true)
    var id : Int? = 0
)
