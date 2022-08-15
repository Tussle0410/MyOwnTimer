package com.tussle.myowntimer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Time(
    @PrimaryKey val title : String,
    @PrimaryKey val date : Date,
    @ColumnInfo(name = "time", defaultValue = "0") val time : Int?
)
