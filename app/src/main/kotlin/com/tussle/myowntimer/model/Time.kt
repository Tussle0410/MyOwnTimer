package com.tussle.myowntimer.model

import androidx.room.*

@Entity
data class Time(
    @PrimaryKey var titie : String,
    @ColumnInfo(name = "todayTime", defaultValue = "0") val todayTime : Int?,
    @ColumnInfo(name = "weekendTime", defaultValue = "0") val weekendTime : Int?,
    @ColumnInfo(name = "monthTime", defaultValue = "0") val monthTime : Int?,
    @ColumnInfo(name = "totalTime", defaultValue = "0") val totalTime : Int?
)
