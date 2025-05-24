package com.appworx.todo.resources

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/todo")
data class Todos(val filter: String? = null)

@Serializable
@Resource("/todo/{id}")
data class TodoById(val id: String)

