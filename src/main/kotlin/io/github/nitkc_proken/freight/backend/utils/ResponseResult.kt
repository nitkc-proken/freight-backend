package io.github.nitkc_proken.freight.backend.utils

import kotlinx.serialization.Serializable

@Serializable
sealed class ResponseResult(val ok: Boolean) {
    @Serializable
    data class Success<T>(
        val data: T
    ) : ResponseResult(true)

    @Serializable
    data class Error(val message: String) : ResponseResult(false)
}

fun <T> T.wrapSuccess(): ResponseResult.Success<T> = ResponseResult.Success(this)
fun responseError(message: String): ResponseResult.Error = ResponseResult.Error(message)
