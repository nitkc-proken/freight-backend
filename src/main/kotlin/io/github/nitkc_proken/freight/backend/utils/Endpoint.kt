package io.github.nitkc_proken.freight.backend.utils

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.documentation
import io.ktor.http.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlin.reflect.KType
import kotlin.reflect.typeOf


@KtorDsl
@JvmName(name = "postTyped")
inline fun <reified Resource : Any, reified T : Validatable, reified Success> Route.documentedPost(
    description: String? = null,
    noinline extend: (OpenApiRoute.() -> Unit)? = null,
    noinline builder: OpenApiRoute.() -> Unit = {
        this.description = description
        request {
            body<T>()
        }
        response {
            HttpStatusCode.OK to {
                body<ResponseResult.Success<Success>>()
            }
        }
        extend?.invoke(this)
    },
    noinline body: suspend RoutingContext.(Resource, ValidatableValue<T>) -> Unit
) = documentation(builder) {
    post<Resource, ValidatableValue<T>> { a, b ->
        body(a, b)
    }
}

@KtorDsl
@JvmName(name = "postTyped")
inline fun <reified Resource : Any, reified Success> Route.documentedPost(
    description: String? = null,
    noinline extend: (OpenApiRoute.() -> Unit)? = null,
    noinline builder: OpenApiRoute.() -> Unit = {
        this.description = description
        response {
            HttpStatusCode.OK to {
                body<ResponseResult.Success<Success>>()
            }
        }
        extend?.invoke(this)
    },
    noinline body: suspend RoutingContext.(Resource) -> Unit
) = documentation(builder) {
    post<Resource> {
        body(it)
    }
}

@KtorDsl
@JvmName(name = "getTyped")
inline fun <reified Resource : Any, reified Success : Any> Route.documentedGet(
    description: String? = null,
    noinline extend: (OpenApiRoute.() -> Unit)? = null,
    noinline builder: (OpenApiRoute.() -> Unit) = {
        this.description = description
        response {
            HttpStatusCode.OK to {
                body<ResponseResult.Success<Success>>()
            }
        }
        extend?.invoke(this)
    },
    noinline body: suspend RoutingContext.(Resource) -> Unit
) = documentation(
    builder
) {
    get<Resource> {
        body(it)
    }
}

fun OpenApiRoute.errorResponses(
    vararg errorResponses: HttpStatusCode
) {
    responses(
        *errorResponses.map { it to typeOf<ResponseResult.Error>() }.toTypedArray()
    )
}

inline fun <reified Success : Any> OpenApiRoute.responses(
    success: HttpStatusCode = HttpStatusCode.OK,
    vararg errorResponses: HttpStatusCode
) {
    responses(
        success to typeOf<Success>(),
        *errorResponses.map { it to typeOf<ResponseResult.Error>() }.toTypedArray()
    )
}

fun OpenApiRoute.responses(
    vararg responses: Pair<HttpStatusCode, KType>
) {
    response {
        responses.forEach { (status, bodyType) ->
            status to {
                description = status.description
                body(bodyType)
            }
        }
    }
}