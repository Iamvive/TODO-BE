package com.appworx.plugins.auth

import io.ktor.server.auth.*
import java.security.MessageDigest

fun AuthenticationConfig.configureDigestAuth() {
    val Realm = "Access protected routes"
    val users: Map<String, ByteArray> = mapOf(
        "user" to "user:$Realm:password".toHashValues(),
        "admin" to "admin:$Realm:password".toHashValues()
    )

    digest("auth-digest") {
        realm = Realm
        digestProvider { userName, _ ->
            users[userName]
        }

        validate {
            if (it.userName.isNotEmpty() && users.containsKey(it.userName)) {
                UserIdPrincipal(it.userName)
            } else {
                null
            }
        }
    }
}

private fun String.toHashValues(): ByteArray =
    MessageDigest.getInstance("MD5")
        .digest(toByteArray())
