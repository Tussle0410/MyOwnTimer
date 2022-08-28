package com.tussle.myowntimer.model

import androidx.room.ColumnInfo
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
)]
)
data class CalendarTime(
    val calendar_title : String,
    val time_date : String,
    @ColumnInfo(name = "time", defaultValue = "0")
    val time : Int = 0,
    @PrimaryKey(autoGenerate = true)
    val id : Int?
)
