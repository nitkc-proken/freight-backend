package io.github.nitkc_proken.freight.backend.auth

import io.github.nitkc_proken.freight.backend.auth.model.LoginCredential
import io.github.nitkc_proken.freight.backend.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.authRouting() {
    val authService by inject<AuthService>()

    routing {
        documentedPost<Auth.Login, LoginCredential, UserWithTokenResponse> { _, credentialValidatableValue ->
            val credential = credentialValidatableValue.validateOrNull() ?: return@documentedPost call.respond(
                status = HttpStatusCode.BadRequest,
                responseError("Invalid Format")
            )
            val userWithToken = authService.login(credential) ?: return@documentedPost call.respond(
                status = HttpStatusCode.Unauthorized,
                responseError("Invalid credential")
            )

            call.respond(userWithToken.toSuccess())
        }
    }
}
