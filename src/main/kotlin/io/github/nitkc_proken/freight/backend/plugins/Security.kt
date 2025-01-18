package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val tokenRepository by inject<TokenRepository>()
    authentication {
        bearer {

            authenticate {
                tokenRepository.getUserFromToken(it.token)
            }
        }
    }
}

