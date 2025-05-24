package com.appworx

import com.appworx.plugins.*
import com.appworx.plugins.auth.configureAuthentications
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureAuthentications()
    configureRateLimiting()
    configureSerialization()
    configureRequestValidation()
    configureResources() // Resource configuration should be done before routing
    configureRouting()
    configureStatusPages()
}
