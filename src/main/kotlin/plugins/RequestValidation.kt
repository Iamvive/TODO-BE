package com.appworx.plugins

import com.appworx.streamfile.validateFileStreamingRequest
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validateFileStreamingRequest()
    }
}
