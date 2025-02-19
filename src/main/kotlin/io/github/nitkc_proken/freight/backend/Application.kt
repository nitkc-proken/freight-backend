package io.github.nitkc_proken.freight.backend

import io.github.nitkc_proken.freight.backend.feature.auth.authModule
import io.github.nitkc_proken.freight.backend.feature.containers.containersModule
import io.github.nitkc_proken.freight.backend.feature.networks.networkModule
import io.github.nitkc_proken.freight.backend.plugins.*
import io.github.nitkc_proken.freight.backend.feature.users.usersModule
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureOpenAPI()

    configureKoin()
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting()


    authModule()
    networkModule()
    usersModule()
    containersModule()

    configureGRPCServer()
}
