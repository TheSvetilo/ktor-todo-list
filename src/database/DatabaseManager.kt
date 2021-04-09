package com.vlad.database

import com.vlad.entities.ToDo
import com.vlad.entities.ToDoDraft
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.first
import org.ktorm.entity.firstOrNull
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList

class DatabaseManager {

    private val hostname = "127.0.0.1"
    private val databaseName = "ktor-db-todo"
    private val username = "vlad"
    private val password = "vlad007"

    private val ktormDatabase: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostname:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktormDatabase = Database.connect(jdbcUrl)
    }

    fun getAllTodos(): List<DBTodoEntity> {
        return ktormDatabase.sequenceOf(DBTodoTable).toList()
    }

    fun getTodo(id: Int) : DBTodoEntity? {
        return ktormDatabase.sequenceOf(DBTodoTable)
            .firstOrNull { it.id eq id }
    }

    fun addTodo(draft: ToDoDraft): ToDo {

        val insertedID = ktormDatabase.insertAndGenerateKey(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
        } as Int

        return ToDo(insertedID, draft.title, draft.done)
    }

    fun updateTodo(id: Int, draft: ToDoDraft): Boolean {
        val updatedRows = ktormDatabase.update(DBTodoTable) {
            set(DBTodoTable.title, draft.title)
            set(DBTodoTable.done, draft.done)
            where {
                it.id eq id
            }
        }
        return updatedRows > 0
    }

    fun removeTodo(id: Int): Boolean {
        val deletedRows = ktormDatabase.delete(DBTodoTable) {
            it.id eq id
        }
        return deletedRows > 0
    }

}