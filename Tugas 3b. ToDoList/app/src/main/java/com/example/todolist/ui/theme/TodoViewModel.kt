package com.example.todolist.ui.theme

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.todolist.Data.TodoItem

class TodoViewModel : ViewModel(){
    val todoList = mutableStateListOf<TodoItem>()
    private var nextId = 1

    fun addTodo(title: String){
        if (title.isNotBlank()){
            todoList.add(TodoItem(id = nextId++, title = title))
        }
    }

    fun toggleTodo(item: TodoItem){
        val index = todoList.indexOf(item)
        if (index!=-1){
            todoList[index] = item.copy(isDone = !item.isDone)
        }
    }

    fun deleteTodo(item: TodoItem){
        todoList.remove(item)
    }
}