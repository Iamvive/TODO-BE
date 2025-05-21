package com.appworx.plugins

import com.appworx.entities.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.respond
import kotlinx.serialization.SerializationException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            val error = mapToErrorResponse(cause)
            val status = mapToStatusCode(cause)

            call.respond(status, error)
        }

        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse("NOT_FOUND", "The requested resource was not found")
            )
        }

        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("UNAUTHORIZED", "Unauthorized access")
            )
        }

        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ErrorResponse(
                    errorCode = "INTERNAL_SERVER_ERROR",
                    errorMessage = cause.message ?: "Unexpected error occurred"
                )
            )
        }

        status(HttpStatusCode.TooManyRequests) { call, httpStatusCode ->
             call.respond(
                httpStatusCode,
                ErrorResponse(
                    errorCode = "TOO_MANY_REQUESTS",
                    errorMessage = "Rate limit exceeded"
                )
            )
        }

    }
}

private fun mapToErrorResponse(cause: Throwable): ErrorResponse = when (cause) {
    is SerializationException -> ErrorResponse("BAD_REQUEST", "Invalid input format")
    is IllegalArgumentException -> ErrorResponse("BAD_REQUEST", cause.message ?: "Invalid argument")
    else -> ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred")
}

private fun mapToStatusCode(cause: Throwable): HttpStatusCode = when (cause) {
    is SerializationException, is IllegalArgumentException -> HttpStatusCode.BadRequest
    else -> HttpStatusCode.InternalServerError
}


