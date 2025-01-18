package io.github.nitkc_proken.freight.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(Resources)
    /*routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }*/
}


