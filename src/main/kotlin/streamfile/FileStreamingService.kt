package com.appworx.streamfile

import com.appworx.streamfile.entites.StreamingData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

class FileStreamingService(private val uploadPath: String = DEFAULT_UPLOAD_DIR) {

    init {
        File(uploadPath).apply { if (!exists()) mkdirs() }
    }

    suspend fun streamFile(fileName: String, call: ApplicationCall) {
        val file = File(uploadPath, fileName)
        if (!file.exists()) call.respondText(status = HttpStatusCode.NotFound, text = "Kuch ni")
        call.respondFile(file)
    }


    private companion object {
        const val DEFAULT_UPLOAD_DIR = "uploads/"
    }
}

fun Route.configureFileStreamRoutes(service: FileStreamingService) {
    get<StreamingData> { resource: StreamingData ->
        service.streamFile(resource.name, call)
    }
}
