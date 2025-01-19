package io.github.nitkc_proken.freight.backend

import io.github.nitkc_proken.freight.backend.auth.authModule
import io.github.nitkc_proken.freight.backend.networks.networkModule
import io.github.nitkc_proken.freight.backend.plugins.*
import io.github.nitkc_proken.freight.backend.users.usersModule
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
}
