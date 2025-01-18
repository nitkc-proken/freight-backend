package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.entity.TokenEntity
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TokenRepository {
    suspend fun createToken(user: User, expiresAt: Instant): Token
    suspend fun getUserFromToken(token: String): User?
}

fun TokenEntity.toModel() = Token(token.value, expiresAt)
@Serializable
data class Token  constructor(
    val token: String,
    val expiresAt: Instant
)
