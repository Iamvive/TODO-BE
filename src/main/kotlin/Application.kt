package com.appworx

import com.appworx.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureRateLimiting()
    configureSerialization()
    configureResources() // Resource configuration should be done before routing
    configureRouting()
    configureStatusPages()
}
