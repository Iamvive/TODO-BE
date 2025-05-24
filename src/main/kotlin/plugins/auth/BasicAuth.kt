package com.appworx.plugins.auth

import io.ktor.server.auth.*

fun AuthenticationConfig.configureBasicAuth() {

    basic("auth-basic") {
        realm = "Access to the '/todo' path"
        validate { credentials ->
            if (credentials.name == "user" && credentials.password == "password") {
                UserIdPrincipal(credentials.name)
            } else null
        }
    }
}
