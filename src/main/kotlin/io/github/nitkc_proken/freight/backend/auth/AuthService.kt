package io.github.nitkc_proken.freight.backend.auth

import io.github.nitkc_proken.freight.backend.auth.model.LoginCredential
import io.github.nitkc_proken.freight.backend.repository.Token
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.User
import io.github.nitkc_proken.freight.backend.repository.UserRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Single
import java.time.LocalDateTime
import java.time.Period
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class AuthService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) {
    companion object {
        private val TokenExpires = Period.ofYears(1)
    }

    suspend fun login(credential: LoginCredential): UserWithTokenResponse? {
        val user = userRepository.findUserByUsername(credential.username)
        if (user?.let {
                it.passwordHash matches credential.password
            } != true
        ) {
            return null
        }
        val token = tokenRepository.createToken(user, Clock.System.now() + 365.days)
        return user.withToken(token)
    }

    fun logout() = Unit

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