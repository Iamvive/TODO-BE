package com.appworx.plugins

import com.appworx.fileupload.FileUploadService
import com.appworx.fileupload.configureFileUploadRoutes
import com.appworx.streamfile.FileStreamingService
import com.appworx.streamfile.configureFileStreamRoutes
import com.appworx.todo.routes.todoRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(RoutingRoot) {
        todoRoutes()
        configureFileUploadRoutes(FileUploadService())
        configureFileStreamRoutes(FileStreamingService())
    }
}