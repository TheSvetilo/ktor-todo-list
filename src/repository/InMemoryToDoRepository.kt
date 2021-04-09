package com.vlad.repository

import com.vlad.entities.ToDo
import com.vlad.entities.ToDoDraft

class InMemoryToDoRepository : ToDoRepository {

    private val todos = mutableListOf<ToDo>()
//        ToDo(1, "Task 1", true),
//        ToDo(2, "Task 2", false),
//        ToDo(3, "Task 3", false)


    override fun getAllTodos(): List<ToDo> {
        return todos
    }

    override fun getTodo(id: Int): ToDo? {
        return todos.firstOrNull { it.id == id }
    }

    override fun addTodo(draft: ToDoDraft): ToDo {
        val toDo = ToDo(
            id = todos.size + 1,
            title = draft.title,
            done = draft.done
        )
        todos.add(toDo)
        return toDo
    }

    override fun removeTodo(id: Int): Boolean {
        return todos.removeIf { it.id == id }
    }

    override fun updateTodo(id: Int, draft: ToDoDraft): Boolean {

        val todo = todos.firstOrNull { it.id == id } ?: return false

        todo.title = draft.title
        todo.done = draft.done

        return true
    }

}