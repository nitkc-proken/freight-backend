package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.entity.TokenEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.util.UUID

@Single
class TokenRepositoryImpl : TokenRepository {
    override suspend fun createToken(user: User, expiresAt: Instant): Token = suspendTransaction {
        TokenEntity.new {
            this.user = user.toEntity()
            this.expiresAt = expiresAt
        }.toModel()
    }

    override suspend fun getUserFromToken(token: String): User? {
        return TokenEntity.findById(token)?.user?.toModel()
    }
}