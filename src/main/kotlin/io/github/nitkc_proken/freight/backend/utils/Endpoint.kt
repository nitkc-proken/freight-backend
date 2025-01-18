package io.github.nitkc_proken.freight.backend.utils

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.documentation
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*

@KtorDsl
@JvmName(name = "postTyped")
inline fun <reified Resource : Any, reified T : Validatable, reified Success : Any> Route.documentedPost(
    noinline builder: OpenApiRoute.() -> Unit = {
        request {
            body<T>()
        }
        response {
            default {
                body<ResponseResult.Error>()
            }
            HttpStatusCode.OK to {
                body<ResponseResult.Success<Success>>()
            }
        }
    },
    noinline body: suspend RoutingContext.(Resource, ValidatableValue<T>) -> Unit
) = documentation(builder) {
    post<Resource, ValidatableValue<T>> { a, b ->
        body(a, b)
    }
}


@KtorDsl
@JvmName(name = "getTyped")
inline fun <reified Resource : Any, reified Success : Any> Route.documentedGet(
    noinline builder: OpenApiRoute.() -> Unit = {
        response {
            default {
                body<ResponseResult.Error>()
            }
            HttpStatusCode.OK to {
                body<ResponseResult.Success<Success>>()
            }
        }
    },
    noinline body: suspend RoutingContext.(Resource) -> Unit
) = documentation(builder) {
    get<Resource> {
        body(it)
    }
}
