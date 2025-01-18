package io.github.nitkc_proken.freight.backend.auth

import io.ktor.server.application.*

fun Application.authModule() {
    authRouting()
}