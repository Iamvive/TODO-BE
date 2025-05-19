package com.appworx.todo.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/todos")
data class Todos(val filter: String? = null)

@Serializable
@Resource("/todos/{id}")
data class TodoById(val id: String)

