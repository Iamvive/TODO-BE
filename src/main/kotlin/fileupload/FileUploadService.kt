package com.appworx.fileupload

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import org.slf4j.LoggerFactory
import java.io.File

class FileUploadService(private val uploadPath: String = DEFAULT_UPLOAD_DIR) {

    private val logger = LoggerFactory.getLogger(FileUploadService::class.java)

    init {
        File(uploadPath).apply { if (!exists()) mkdirs() }
    }

    suspend fun storeSingleFile(call: ApplicationCall) {
        val file = File(uploadPath, "upload_${System.currentTimeMillis()}.bin")
        logger.info("Storing single file at: ${file.absolutePath}")

        call.receiveChannel().copyAndClose(file.writeChannel())
    }

    suspend fun handleMultipart(call: ApplicationCall) {
        val multipart = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 50) // 10 MB limit

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> uploadMultipartFile(part)
                else -> Unit
            }
            part.dispose()
        }
    }

    private suspend fun uploadMultipartFile(part: PartData.FileItem) {
        val fileName = sanitizeFilename(part.originalFileName ?: "uploaded_file")
        val file = File(uploadPath, fileName)

        logger.info("Saving file: ${file.absolutePath}")

        part.provider().copyAndClose(file.writeChannel())
    }

    private fun sanitizeFilename(name: String): String =
        name.replace(Regex("[^A-Za-z0-9_.-]"), "_")

    private companion object {
        const val DEFAULT_UPLOAD_DIR = "uploads/"
    }
}

fun Route.configureFileUploadRoutes(service: FileUploadService) {
    post("upload/single") {
        runCatching { service.storeSingleFile(call) }
            .onSuccess { call.respond(HttpStatusCode.OK, "File uploaded successfully.") }
            .onFailure {
                serviceErrorLogger(it)
                call.respond(HttpStatusCode.InternalServerError, "Failed to upload file.")
            }
    }

    post("upload/multipart") {
        runCatching { service.handleMultipart(call) }
            .onSuccess { call.respond(HttpStatusCode.OK, "Files uploaded successfully.") }
            .onFailure {
                serviceErrorLogger(it)
                call.respond(HttpStatusCode.InternalServerError, "Failed to upload files.")
            }
    }
}

private fun serviceErrorLogger(e: Throwable) {
    LoggerFactory.getLogger("FileUploadService").error("Upload error: ${e.message}", e)
}
