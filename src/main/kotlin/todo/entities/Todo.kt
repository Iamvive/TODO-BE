package com.appworx.todo.entities

import kotlinx.serialization.Serializable

@Serializable
data class Todo(
    val id: String,
    val title: String,
    val completed: Boolean,
)
