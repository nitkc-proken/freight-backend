package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.entity.TokenEntity
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.koin.core.annotation.Single
import kotlin.uuid.ExperimentalUuidApi

@Single
class TokenRepositoryImpl : TokenRepository {
    override suspend fun createToken(user: User, expiresAt: Instant): Token = suspendTransaction {
        val userEntity = user.toEntity()
        TokenEntity.new {
            this.user = userEntity
            this.expiresAt = expiresAt
        }.toModel()
    }

    override suspend fun getUserFromToken(token: String): User? {
        return TokenEntity.findById(token)?.user?.toModel()
    }
}
