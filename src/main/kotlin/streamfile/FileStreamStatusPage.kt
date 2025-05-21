package com.appworx.streamfile

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respond

fun StatusPagesConfig.configureFileStreamStatusPages() {
    exception<RequestValidationException> { call, cause ->
        call.respond(
            HttpStatusCode.BadRequest,
            mapOf(
                "error" to "Validation failed",
                "details" to cause.reasons
            )
        )
    }

    exception<NotFoundException> { call, cause ->
        call.respond(
            status = HttpStatusCode.NotFound,
            message = cause.message ?: "File not found"
        )
    }

    exception<IllegalArgumentException> { call, cause ->
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = cause.message ?: "Invalid argument"
        )
    }

    exception<Throwable> { call, cause ->
        call.respond(
            status = HttpStatusCode.InternalServerError,
            message = cause.message ?: "An unexpected error occurred"
        )
    }
}
