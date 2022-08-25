package com.tussle.myowntimer.model

import androidx.room.*

@Entity
data class Title(
    @PrimaryKey
    var title : String = "",
    val todayTime : Int = 0,
    val monthTime : Int = 0,
    val totalTime : Int = 0,
)
