package io.github.nitkc_proken.freight.backend.auth

import io.github.nitkc_proken.freight.backend.plugins.KoinModule
import io.github.nitkc_proken.freight.backend.repository.Token
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.User
import io.github.nitkc_proken.freight.backend.testutils.clientWithDefault
import io.github.nitkc_proken.freight.backend.testutils.configureRoutingTest
import io.github.nitkc_proken.freight.backend.feature.users.usersModule
import io.github.nitkc_proken.freight.backend.values.Argon2
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import kotlinx.datetime.Instant
import org.koin.dsl.module
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UsersRoutingKtTest {
    val testModule = module(true) {
        single<TokenRepository> {
            object : TokenRepository {
                val user = User(
                    Uuid.random(),
                    "test",
                    Argon2.hash("password")
                )
                override suspend fun createToken(user: User, expiresAt: Instant): Token {
                    return Token("test", expiresAt)
                }

                override suspend fun getUserFromValidToken(token: String): User {
                    return user
                }

                override suspend fun expireTokenFromUser(token: String): Boolean {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    @Test
    fun testGetUserSelf() = testApplication {
        application {
            install(Koin) {
                slf4jLogger()
                modules(
                    KoinModule().module,
                    testModule
                )
            }
            configureRoutingTest()
            usersModule()
        }
        val client = clientWithDefault()
        client.get("/users/me") {
            bearerAuth("test")
        }.apply {
            println(this.bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
