package com.tussle.myowntimer.model

import androidx.room.Embedded

data class TitleAndTodo(
    @Embedded
    val title : Title,
    @Embedded
    val todo : Todo?
)
