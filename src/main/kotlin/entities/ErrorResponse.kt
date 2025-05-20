package com.appworx.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("error_code") val errorCode: String,
    @SerialName("error_message") val errorMessage: String,
)
