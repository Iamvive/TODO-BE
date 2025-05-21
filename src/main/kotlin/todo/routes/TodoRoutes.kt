package com.appworx.todo.routes

import com.appworx.routing.toEntity
import com.appworx.todo.entities.Todo
import com.appworx.todo.resources.TodoById
import com.appworx.todo.resources.Todos
import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.Route

fun Route.todoRoutes() {
    val todos = mutableListOf<Todo>()

    rateLimit(RateLimitName("public")) {
        get<Todos> {
          call.respond(todos)
        }
    }

    rateLimit(RateLimitName("private")) {
        post<Todos> {
            val todo = call.toEntity<Todo>()
            todos.add(todo)
            call.respond(HttpStatusCode.Created, todo)
        }
    }

    rateLimit(RateLimitName("protected")) {
        get<TodoById> { resource ->
          val todo = todos.find { it.id == resource.id }
                ?: throw NotFoundException("Todo not found")
            call.respond(todo)
        }
    }


    rateLimit(RateLimitName("private")) {
        put<TodoById> { resource ->
             val updatedTodo = call.receive<Todo>()
            val index = todos.indexOfFirst { it.id == resource.id }
            if (index != -1) {
                todos[index] = updatedTodo
                call.respond(HttpStatusCode.OK, updatedTodo)
            } else {
                throw NotFoundException("Todo not found")
            }
        }
    }


    rateLimit(RateLimitName("public")) {
        delete<TodoById> { resource ->
            val removed = todos.removeIf { it.id == resource.id }
            if (removed) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                throw NotFoundException("Todo not found")
            }
        }
    }
}

