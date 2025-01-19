package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.entity.TokenEntity
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import kotlinx.datetime.Instant
import org.koin.core.annotation.Single

@Single
class TokenRepositoryImpl : TokenRepository {
    override suspend fun createToken(user: User, expiresAt: Instant): Token = suspendTransaction {
        val userEntity = user.toEntity()
        TokenEntity.new {
            this.user = userEntity
            this.expiresAt = expiresAt
        }.toModel()
    }

    override suspend fun getUserFromToken(token: String): User? = suspendTransaction {
        TokenEntity.findById(token)?.user?.toModel()
    }

    override suspend fun expireTokenFromUser(token: String): Boolean = suspendTransaction {
        val tokenEntity = TokenEntity.findById(token)
        tokenEntity?.delete() ?: return@suspendTransaction false
        true
    }
}
