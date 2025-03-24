package io.github.nitkc_proken.freight.backend.feature.containers

import io.github.nitkc_proken.freight.backend.feature.containers.model.CreateContainerRequest
import io.github.nitkc_proken.freight.backend.plugins.UserWithToken
import io.github.nitkc_proken.freight.backend.repository.Container
import io.github.nitkc_proken.freight.backend.utils.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val containerService by inject<ContainerService>()
    routing {
        authenticate {
            documentedPost<Containers.Create, CreateContainerRequest, Container>(
                "コンテナを作成する",
                {
                    responses<Container>(
                        success = HttpStatusCode.Created,
                        HttpStatusCode.Unauthorized,
                        HttpStatusCode.BadRequest,
                        HttpStatusCode.InternalServerError
                    )
                }
            ) { _, createContainerRequestBody ->
                val principal = call.principal<UserWithToken>() ?: return@documentedPost call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )
                val createContainerRequest = createContainerRequestBody.validateOrNull()
                    ?: return@documentedPost call.respond(
                        status = HttpStatusCode.BadRequest,
                        responseError("Invalid Format")
                    )
                val result = containerService.createContainer(principal.user, createContainerRequest)
                    ?: return@documentedPost call.respond(
                        status = HttpStatusCode.InternalServerError,
                        responseError("Failed to create container")
                    )
                call.respond(HttpStatusCode.Created, result.wrapSuccess())
            }
        }
    }
}
