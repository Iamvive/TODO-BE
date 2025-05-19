package com.appworx.todo.routes

import com.appworx.routing.toEntity
import com.appworx.todo.entities.Todo
import com.appworx.todo.resources.TodoById
import com.appworx.todo.resources.Todos
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.resources.get
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.delete

fun Route.todoRoutes() {
    val todos = mutableListOf<Todos>()

    // GET /todos
    get<Todos> {
        println("Received a GET request for all todos")
        call.respond(todos)
    }

    // POST /todos
    post<Todos> {
        println("Received a POST request to create a new todo")
        val todo = call.toEntity<Todo>()
        todos.add(todo)
        call.respond(HttpStatusCode.Created, todo)
    }

    // GET /todos/{id}
    get<TodoById> { resource ->
        println("Received a GET request for todo with ID: ${resource.id}")
        val todo = todos.find { it.id == resource.id }
        if (todo != null) {
            call.respond(todo)
        } else {
            call.respond(HttpStatusCode.NotFound, "Todo not found")
        }
    }

    // PUT /todos/{id}
    put<TodoById> { resource ->
        println("Received a PUT request to update todo with ID: ${resource.id}")
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
        println("Received a DELETE request for todo with ID: ${resource.id}")
        val removed = todos.removeIf { it.id == resource.id }
        if (removed) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound, "Todo not found")
        }
    }
}


