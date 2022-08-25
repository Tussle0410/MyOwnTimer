package com.tussle.myowntimer.model

data class ViewPagerModel(
    val title : String,
    var goalTodo : String,
    var allTodo : String,
    val todo : MutableList<String?>,
    val todoSuccess : MutableList<Boolean?>,
    val todayTime : String,
    val monthTime : String,
    val totalTime : String
)
