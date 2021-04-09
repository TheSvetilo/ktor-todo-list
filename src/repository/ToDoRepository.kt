package com.vlad.repository

import com.vlad.entities.ToDo
import com.vlad.entities.ToDoDraft

interface ToDoRepository {

    fun getAllTodos() : List<ToDo>

    fun getTodo(id: Int) : ToDo?

    fun addTodo(draft: ToDoDraft): ToDo

    fun removeTodo(id: Int) : Boolean

    fun updateTodo(id: Int, draft: ToDoDraft): Boolean
}