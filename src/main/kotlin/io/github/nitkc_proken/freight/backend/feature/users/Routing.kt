package io.github.nitkc_proken.freight.backend.feature.users

import io.github.nitkc_proken.freight.backend.plugins.UserWithToken
import io.github.nitkc_proken.freight.backend.utils.documentedGet
import io.github.nitkc_proken.freight.backend.utils.errorResponses
import io.github.nitkc_proken.freight.backend.utils.responseError
import io.github.nitkc_proken.freight.backend.utils.wrapSuccess
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.usersRouting() {
    val userService by inject<UserService>()
    routing {
        authenticate {
            documentedGet<Users.Me, UserResponse>(
                description = "ユーザー情報を取得する",
                {
                    errorResponses(HttpStatusCode.Unauthorized)
                }
            ) {
                val principal = call.principal<UserWithToken>() ?: return@documentedGet call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )
                call.respond(userService.getUserSelf(principal.user).wrapSuccess())
            }
        }
    }
}
