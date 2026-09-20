package com.example.todolist.Data

data class TodoItem(
    val id: Int,
    val title: String,
    val isDone: Boolean = false
)
