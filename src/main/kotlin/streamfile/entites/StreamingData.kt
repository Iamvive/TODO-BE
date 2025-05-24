package com.appworx.streamfile.entites

import io.ktor.resources.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Resource("streaming")
@Serializable
data class StreamingData(
   @SerialName("file_name") val name: String,
)
