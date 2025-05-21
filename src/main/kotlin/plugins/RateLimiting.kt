package com.appworx.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.ratelimit.RateLimitName
import kotlin.time.Duration.Companion.seconds

fun Application.configureRateLimiting() {
    install(RateLimit) {
        register(RateLimitName("private")) {
            rateLimiter(limit = 5, refillPeriod = 20.seconds)
        }

        register(RateLimitName("protected")) {
            rateLimiter(limit = 10, refillPeriod = 20.seconds)
        }

        register(RateLimitName("public")) {
            rateLimiter(limit = 20, refillPeriod = 2.seconds)
        }
    }
}
