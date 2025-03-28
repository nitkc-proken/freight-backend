package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import io.github.nitkc_proken.freight.backend.entity.TokenEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.Token
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.User
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.repository.toModel
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import kotlinx.datetime.Instant
import kotlinx.datetime.isDistantPast
import org.koin.core.annotation.Single

@Single
class TokenRepositoryImpl : TokenRepository {
    override suspend fun createToken(user: User, expiresAt: Instant): Token = suspendTransaction {
        val userEntity = user.toEntityId()
        TokenEntity.new {
            this.user = UserEntity[userEntity]
            this.expiresAt = expiresAt
        }.toModel()
    }

    override suspend fun getUserFromValidToken(token: String): User? = suspendTransaction {
        val tokenEntity = TokenEntity.find { TokensTable.token eq token }.singleOrNull()
        if (tokenEntity?.expiresAt?.isDistantPast == true) {
            tokenEntity.delete()
            return@suspendTransaction null
        }
        tokenEntity?.user?.toModel()
    }

    override suspend fun expireTokenFromUser(token: String): Boolean = suspendTransaction {
        val tokenEntity = TokenEntity.find { TokensTable.token eq token }.singleOrNull()
        if (tokenEntity == null) return@suspendTransaction false
        tokenEntity.delete()
        true
    }
}
