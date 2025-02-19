package io.github.nitkc_proken.freight.backend.feature.networks

import io.github.nitkc_proken.freight.backend.grpc.gatewayClient
import io.github.nitkc_proken.freight.backend.repository.Network
import io.github.nitkc_proken.freight.backend.repository.NetworkRepository
import io.github.nitkc_proken.freight.backend.repository.toModel
import io.ktor.server.application.*
import kotlinx.coroutines.*
import org.koin.ktor.ext.inject

fun Application.networkModule() {
    val networkRepository by inject<NetworkRepository>()


    configureRouting()
}



