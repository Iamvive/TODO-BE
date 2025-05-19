package com.appworx.plugins

import com.appworx.todo.routes.todoRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(RoutingRoot) {
        todoRoutes()
    }
}