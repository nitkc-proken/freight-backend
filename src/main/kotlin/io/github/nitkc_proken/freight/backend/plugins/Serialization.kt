package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.utils.ResponseResult
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val responseModule = SerializersModule {
    polymorphic(ResponseResult::class) {
        subclass(ResponseResult.Success.serializer(PolymorphicSerializer(Any::class)))
        subclass(ResponseResult.Error.serializer())
    }
}
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    /*routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }*/
}
