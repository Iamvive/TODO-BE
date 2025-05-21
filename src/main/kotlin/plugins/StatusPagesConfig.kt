package com.appworx.plugins

import com.appworx.common.centralStatusConfig
import com.appworx.streamfile.configureFileStreamStatusPages
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.configureStatusPages() {
    install(StatusPages) {
        configureFileStreamStatusPages()
        centralStatusConfig()
    }
}
