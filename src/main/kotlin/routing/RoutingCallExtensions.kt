package com.appworx.routing

import io.ktor.server.request.*
import io.ktor.server.routing.*

suspend inline fun<reified T : Any> RoutingCall.toEntity() =
    receive<T>()