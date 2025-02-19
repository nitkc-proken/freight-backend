package io.github.nitkc_proken.freight.backend.feature.auth

import io.github.nitkc_proken.freight.backend.database.tables.UsersTable.passwordHash
import io.github.nitkc_proken.freight.backend.feature.auth.model.LoginCredential
import io.github.nitkc_proken.freight.backend.repository.Token
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.User
import io.github.nitkc_proken.freight.backend.repository.UserRepository
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class AuthService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    companion object {
        val TokenExpires = 365.days
    }

    suspend fun login(credential: LoginCredential): UserWithTokenResponse? {
        val (user, passwordHash) = userRepository.getUserWithPasswordHash(credential.username) ?: return null

        if (!(passwordHash matches credential.password)) {
            return null
        }
        val token = tokenRepository.createToken(user, Clock.System.now() + TokenExpires)
        return user.withToken(token)
    }

    suspend fun logout(token: String): Boolean {
        return tokenRepository.expireTokenFromUser(token)
    }

    fun register() = Unit
}

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class UserWithTokenResponse(
    val userId: Uuid,
    val username: String,
    val token: Token
)

@OptIn(ExperimentalUuidApi::class)
fun User.withToken(token: Token) = UserWithTokenResponse(id, username, token)
