package com.appworx

import com.appworx.plugins.configureResources
import com.appworx.plugins.configureRouting
import com.appworx.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureResources()
}
