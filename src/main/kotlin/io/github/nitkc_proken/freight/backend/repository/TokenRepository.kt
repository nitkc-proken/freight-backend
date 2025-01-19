package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.entity.TokenEntity
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

interface TokenRepository {
    suspend fun createToken(user: User, expiresAt: Instant): Token
    suspend fun getUserFromToken(token: String): User?
    suspend fun expireTokenFromUser(token: String): Boolean
}

fun TokenEntity.toModel() = Token(token.value, expiresAt)

@Serializable
data class Token constructor(
    val token: String,
    val expiresAt: Instant
)
