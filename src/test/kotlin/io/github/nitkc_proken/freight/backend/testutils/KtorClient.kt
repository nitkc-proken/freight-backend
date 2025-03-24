package io.github.nitkc_proken.freight.backend.testutils

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

fun ApplicationTestBuilder.clientWithDefault() = createClient {
    install(ContentNegotiation) {
        json()
    }
}
