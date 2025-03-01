package io.github.nitkc_proken.freight.backend.auth

import io.github.nitkc_proken.freight.backend.feature.auth.model.LoginCredential
import io.github.nitkc_proken.freight.backend.feature.auth.authModule
import io.github.nitkc_proken.freight.backend.plugins.KoinModule
import io.github.nitkc_proken.freight.backend.repository.Token
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.User
import io.github.nitkc_proken.freight.backend.repository.UserRepository
import io.github.nitkc_proken.freight.backend.testutils.clientWithDefault
import io.github.nitkc_proken.freight.backend.testutils.configureRoutingTest
import io.github.nitkc_proken.freight.backend.values.Argon2
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.client.utils.EmptyContent.status
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
class AuthRoutingKtTest {
    val testModule = module(true) {
        single<UserRepository> {
            object : UserRepository {
                val user = User(
                    Uuid.random(),
                    "test",
                )

                override suspend fun findUserById(id: Uuid): User? {
                    TODO("Not yet implemented")
                }

                override suspend fun findUserByUsername(username: String): User {
                    return user
                }

                override suspend fun getUserWithPasswordHash(username: String): Pair<User, Argon2>? {
                    return user to Argon2.hash("password")
                }

                override suspend fun createUser(username: String, password: Argon2): User {
                    return user
                }
            }
        }
        single<TokenRepository> {
            object : TokenRepository {
                override suspend fun createToken(user: User, expiresAt: Instant): Token {
                    return Token("test", expiresAt)
                }
                override suspend fun getUserFromValidToken(token: String): User? {
                    TODO("Not yet implemented")
                }

                override suspend fun expireTokenFromUser(token: String): Boolean {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    @Test
    fun testPostAuthLogin() = testApplication {
        application {
            install(Koin) {
                slf4jLogger()
                modules(
                    KoinModule().module,
                    testModule
                )
            }
            configureRoutingTest()
            authModule()
        }
        val client = clientWithDefault()
        client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody((LoginCredential("test", "password")))
        }.apply {
            println(this.bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}
