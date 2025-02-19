package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val tokenRepository by inject<TokenRepository>()
    authentication {
        bearer {
            authenticate {
                val user = tokenRepository.getUserFromValidToken(it.token) ?: return@authenticate null
                UserWithToken(user, it.token)
            }
        }
    }
}

data class UserWithToken(
    val user: User,
    val token: String
)