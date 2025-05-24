package com.appworx.streamfile

import com.appworx.streamfile.entites.StreamingData
import io.ktor.server.plugins.requestvalidation.*

fun RequestValidationConfig.validateFileStreamingRequest() {
    val allowedExtensions = setOf("png", "jpg", "jpeg", "pdf", "mp4")

    validate<StreamingData> { req: StreamingData ->
        val name = req.name
        println("Configuring request validation for file streaming with name: $name")

        when {
            name.length > 3 -> ValidationResult.Invalid("File name is too long")
            !name.contains('.') -> ValidationResult.Invalid("File name must contain an extension")
            name.substringAfterLast('.').lowercase() !in allowedExtensions ->
                ValidationResult.Invalid("Unsupported file type: must be one of ${allowedExtensions.joinToString(", ")}")
            else -> ValidationResult.Valid
        }
    }
}
