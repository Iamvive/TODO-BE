package com.appworx.plugins.auth

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentications() {
    authentication {
        configureDigestAuth()
        configureBasicAuth()
    }
}
