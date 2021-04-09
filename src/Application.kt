package com.vlad

import com.vlad.entities.ToDo
import com.vlad.entities.ToDoDraft
import com.vlad.repository.InMemoryToDoRepository
import com.vlad.repository.MySQLTodoRepository
import com.vlad.repository.ToDoRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    routing {

//        val repository: ToDoRepository = InMemoryToDoRepository()
        val repository: ToDoRepository = MySQLTodoRepository()

        get("/") {

        }

        get("/todos") {
            call.respond(repository.getAllTodos())
        }

        get("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    "id's not a number!"
                )
                return@get
            }

            val todo = repository.getTodo(id)
            if (todo == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    "Not found $id"
                )
            } else {
                call.respond(todo)
            }

        }

        post("/todos") {

            val todoDraft = call.receive<ToDoDraft>()

            val todo = repository.addTodo(todoDraft)
            call.respond(todo)

        }

        put("/todos/{id}") {

            val todoDraft = call.receive<ToDoDraft>()
            val todoId = call.parameters["id"]?.toIntOrNull()

            if (todoId == null) {
                call.respond(HttpStatusCode.BadRequest, "Bad request")
                return@put
            }

            val updated = repository.updateTodo(todoId, todoDraft)
            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound,
                "Can't be uploaded.")
            }
        }

        delete("/todos/{id}") {
            val todoId = call.parameters["id"]?.toIntOrNull()

            if (todoId == null) {
                call.respond(HttpStatusCode.BadRequest, "Bad request")
                return@delete
            }

            val removed = repository.removeTodo(todoId)
            if (removed) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound,
                "Can't be deleted.")
            }
        }
    }

}

