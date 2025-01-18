package io.github.nitkc_proken.freight.backend.users

import io.github.nitkc_proken.freight.backend.repository.User
import io.github.nitkc_proken.freight.backend.utils.documentedGet
import io.github.nitkc_proken.freight.backend.utils.responseError
import io.github.nitkc_proken.freight.backend.utils.toSuccess
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.usersRouting() {
    val userService by inject<UserService>()
    routing {
        authenticate {
            documentedGet<Users.Me, UserResponse> {
                val principal = call.principal<User>() ?: return@documentedGet call.respond(
                    responseError("User not found")
                )
                call.respond(userService.getUserSelf(principal).toSuccess())
            }
        }
    }
}