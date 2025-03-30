package io.github.nitkc_proken.freight.backend.feature.networks

import io.github.nitkc_proken.freight.backend.feature.auth.UserWithTokenResponse
import io.github.nitkc_proken.freight.backend.feature.networks.model.CreateNetworkRequest
import io.github.nitkc_proken.freight.backend.plugins.UserWithToken
import io.github.nitkc_proken.freight.backend.repository.Network
import io.github.nitkc_proken.freight.backend.utils.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val networkService by inject<NetworkService>()
    routing {
        authenticate {
            documentedPost<Networks.Create, CreateNetworkRequest, UserWithTokenResponse>(
                "ネットワークを作成する",
                {
                    errorResponses(HttpStatusCode.Unauthorized, HttpStatusCode.BadRequest)
                }
            ) { _, createNetworkRequestBody ->
                val principal = call.principal<UserWithToken>() ?: return@documentedPost call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )
                val createNetworkRequest = createNetworkRequestBody.validateOrNull()
                    ?: return@documentedPost call.respond(
                        status = HttpStatusCode.BadRequest,
                        responseError("Invalid Format")
                    )
                val network = networkService.createNetwork(principal.user, createNetworkRequest)
                call.respond(network.wrapSuccess())
            }
            documentedGet<Networks.GetSelfNetworks, List<Network>>(
                "自身のネットワークを取得する",
                {
                    errorResponses(HttpStatusCode.Unauthorized)
                }
            ) {
                val principal = call.principal<UserWithToken>() ?: return@documentedGet call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )
                val networks = networkService.getNetworks(principal.user)
                call.respond(networks.wrapSuccess())
            }

            documentedGet<Networks.ByOwnerAndName, Network>(
                "指定したネットワークを取得する",
                {
                    request {
                        pathParameter<String>("owner") {
                            description = "ネットワークの名前"
                        }
                        pathParameter<String>("name") {
                            description = "ネットワークの名前"
                        }
                    }
                    errorResponses(HttpStatusCode.Unauthorized, HttpStatusCode.NotFound)
                }
            ) {
                val principal = call.principal<UserWithToken>() ?: return@documentedGet call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )

                val network = networkService.getJoinedNetworkByOwnerAndName(
                    principal.user,
                    it.owner,
                    it.name
                ) ?: return@documentedGet call.respond(
                    status = HttpStatusCode.NotFound,
                    responseError("Network not found")
                )
                call.respond(network.wrapSuccess())
            }

            documentedPost<Networks.ByOwnerAndName.Init, Unit>(
                "指定したネットワークを起動する",
                {
                    request {
                        pathParameter<String>("owner")
                        pathParameter<String>("name")
                    }
                    errorResponses(HttpStatusCode.Unauthorized, HttpStatusCode.NotFound)
                }
            ) {
                val principal = call.principal<UserWithToken>() ?: return@documentedPost call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )

                val network = networkService.getJoinedNetworkByOwnerAndName(
                    principal.user,
                    it.owner,
                    it.name
                ) ?: return@documentedPost call.respond(
                    status = HttpStatusCode.NotFound,
                    responseError("Network not found")
                )
                networkService.initNetwork(network)
                call.respond(HttpStatusCode.OK, Unit.wrapSuccess())
            }

            documentedPost<Networks.ByOwnerAndName.Clean, Unit>(
                "指定したネットワークをシャットダウンする",
                {
                    request {
                        pathParameter<String>("owner")
                        pathParameter<String>("name")
                    }
                    errorResponses(HttpStatusCode.Unauthorized, HttpStatusCode.NotFound)
                }
            ) {
                val principal = call.principal<UserWithToken>() ?: return@documentedPost call.respond(
                    status = HttpStatusCode.Unauthorized,
                    responseError("User not found")
                )

                val network = networkService.getJoinedNetworkByOwnerAndName(
                    principal.user,
                    it.owner,
                    it.name
                ) ?: return@documentedPost call.respond(
                    status = HttpStatusCode.NotFound,
                    responseError("Network not found")
                )
                networkService.cleanUpNetwork(network)

                call.respond(HttpStatusCode.OK, Unit.wrapSuccess())
            }
        }
    }
}
