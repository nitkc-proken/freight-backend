package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.grpc.BackendServer
import io.github.nitkc_proken.freight.utils.dotenv
import io.ktor.server.application.*

fun Application.configureGRPCServer() {
    BackendServer(
        port = dotenv["GRPC_PORT"].toInt(),
    ).start()
}
