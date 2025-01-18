package io.github.nitkc_proken.freight.backend

import io.github.nitkc_proken.freight.backend.auth.authModule
import io.github.nitkc_proken.freight.backend.network.networkModule
import io.github.nitkc_proken.freight.backend.plugins.*
import io.github.nitkc_proken.freight.backend.users.usersModule
import io.github.smiley4.ktorswaggerui.dsl.config.PluginConfigDsl
import io.github.smiley4.ktorswaggerui.routing.ApiSpec
import io.ktor.server.application.*
import java.io.File

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
