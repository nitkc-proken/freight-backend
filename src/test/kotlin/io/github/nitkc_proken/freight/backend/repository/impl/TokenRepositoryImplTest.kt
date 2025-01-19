package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.auth.AuthService
import io.github.nitkc_proken.freight.backend.database.getDBConfigFromEnv
import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.plugins.KoinModule
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.toModel
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.StringSpec
import io.kotest.koin.KoinExtension
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject

class TokenRepositoryImplTest : StringSpec(), KoinTest {
    override fun extensions(): List<Extension> =
        listOf(KoinExtension(KoinModule().module))

    private val tokenRepository by inject<TokenRepository>()

    init {

        "testCreateToken" {
            val db = getDBConfigFromEnv().connect()
            newSuspendedTransaction {
                val user = UserEntity.wrapRow(UsersTable.selectAll().first()).toModel()
                val token = tokenRepository.createToken(user, Clock.System.now() + AuthService.TokenExpires)
                println(token)
            }
        }

    }
}
