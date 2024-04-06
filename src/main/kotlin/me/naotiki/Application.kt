package me.naotiki

import io.ktor.server.application.*
import me.naotiki.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureDatabases()
    configureSecurity()
    configureRouting()
}
