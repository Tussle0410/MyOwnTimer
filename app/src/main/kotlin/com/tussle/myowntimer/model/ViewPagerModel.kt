package com.tussle.myowntimer.model

data class ViewPagerModel(
    var title : String,
    var goalTodo : String,
    var allTodo : String,
    val todo : MutableList<String?>,
    val todoSuccess : MutableList<Boolean?>,
    val todayTime : Int,
    val monthTime : Int,
    val totalTime : Int
)
