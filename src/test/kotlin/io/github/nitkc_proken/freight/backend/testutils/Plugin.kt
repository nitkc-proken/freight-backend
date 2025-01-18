package io.github.nitkc_proken.freight.backend.testutils

import io.github.nitkc_proken.freight.backend.plugins.configureRouting
import io.github.nitkc_proken.freight.backend.plugins.configureSecurity
import io.github.nitkc_proken.freight.backend.plugins.configureSerialization
import io.ktor.server.application.*

fun Application.configureRoutingTest() {
    configureSecurity()
    configureRouting()
    configureSerialization()
}
