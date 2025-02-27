package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import io.github.nitkc_proken.freight.backend.values.Argon2
import org.koin.core.annotation.Single
import java.util.*
import kotlin.uuid.Uuid

@Single
class UserRepositoryImpl : UserRepository {
    override suspend fun findUserById(id: Uuid): User? = suspendTransaction {
        UserEntity.findById(id)?.toModel()
    }

    override suspend fun findUserByUsername(username: String): User? = suspendTransaction {
        UserEntity.find { UsersTable.username eq username }.firstOrNull()?.toModel()
    }

    override suspend fun getUserWithPasswordHash(username: String): Pair<User, Argon2>? = suspendTransaction {
        UserEntity.find { UsersTable.username eq username }.firstOrNull()?.let {
            it.toModel() to it.passwordHash
        }
    }

    override suspend fun createUser(username: String, password: Argon2): User = suspendTransaction {
        UserEntity.new {
            this.username = username
            this.passwordHash = password
        }.toModel()
    }
}
