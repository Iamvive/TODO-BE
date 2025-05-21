package com.appworx.common

import com.appworx.entities.ErrorResponse
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun StatusPagesConfig.centralStatusConfig() {
    exception<Throwable> { call, cause ->
        val error = mapToErrorResponse(cause)
        val status = mapToStatusCode(cause)
        call.respond(status, error)
    }

    status(HttpStatusCode.NotFound) { call, _ ->
        call.respond(
            HttpStatusCode.NotFound,
            ErrorResponse(HttpStatusCode.NotFound.value, "The requested resource was not found")
        )
    }

    status(HttpStatusCode.Unauthorized) { call, _ ->
        call.respond(
            HttpStatusCode.Unauthorized,
            ErrorResponse(HttpStatusCode.Unauthorized.value, "Unauthorized access")
        )
    }

    exception<Throwable> { call, cause ->
        call.respond(
            status = HttpStatusCode.InternalServerError,
            message = ErrorResponse(
                errorCode = HttpStatusCode.InternalServerError.value,
                errorMessage = cause.message ?: "Unexpected error occurred"
            )
        )
    }

    status(HttpStatusCode.TooManyRequests) { call, httpStatusCode ->
        call.respond(
            httpStatusCode,
            ErrorResponse(
                errorCode = HttpStatusCode.TooManyRequests.value,
                errorMessage = "Rate limit exceeded"
            )
        )
    }
}

private fun mapToErrorResponse(cause: Throwable): ErrorResponse = when (cause) {
    is SerializationException -> ErrorResponse(HttpStatusCode.BadRequest.value, "Invalid input format")
    is IllegalArgumentException -> ErrorResponse(HttpStatusCode.BadRequest.value, cause.message ?: "Invalid argument")
    else -> ErrorResponse(HttpStatusCode.InternalServerError.value, "An unexpected error occurred")
}

private fun mapToStatusCode(cause: Throwable): HttpStatusCode = when (cause) {
    is SerializationException, is IllegalArgumentException -> HttpStatusCode.BadRequest
    else -> HttpStatusCode.InternalServerError
}
