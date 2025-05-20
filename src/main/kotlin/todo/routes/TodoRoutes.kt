package com.appworx.todo.routes

import com.appworx.routing.toEntity
import com.appworx.todo.entities.Todo
import com.appworx.todo.resources.TodoById
import com.appworx.todo.resources.Todos
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.todoRoutes() {
    val todos = mutableListOf<Todo>()

    // GET /todos
    get<Todos> {
        call.respond(todos)
    }

    // POST /todos
    post<Todos> {
        val todo = call.toEntity<Todo>()
        todos.add(todo)
        call.respond(HttpStatusCode.Created, todo)
    }

    // GET /todos/{id}
    get<TodoById> { resource ->
        val todo = todos.find { it.id == resource.id }
        if (todo != null) {
            call.respond(todo)
        } else {
            call.respond(HttpStatusCode.NotFound, "Todo not found")
        }
    }

    // PUT /todos/{id}
    put<TodoById> { resource ->
        val updatedTodo = call.receive<Todo>()
        val index = todos.indexOfFirst { it.id == resource.id }
        if (index != -1) {
            todos[index] = updatedTodo
            call.respond(HttpStatusCode.OK, updatedTodo)
        } else {
            call.respond(HttpStatusCode.NotFound, "Todo not found")
        }
    }

    // DELETE /todos/{id}
    delete<TodoById> { resource ->
        val removed = todos.removeIf { it.id == resource.id }
        if (removed) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound, "Todo not found")
        }
    }
}


