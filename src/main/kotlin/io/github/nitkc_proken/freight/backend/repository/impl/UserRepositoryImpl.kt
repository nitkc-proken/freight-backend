package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import io.github.nitkc_proken.freight.backend.values.Argon2
import org.koin.core.annotation.Single
import java.util.*

@Single
class UserRepositoryImpl : UserRepository {
    override suspend fun findUserById(id: UUID): User? = suspendTransaction {
        UserEntity.findById(id)?.toModel()
    }

    override suspend fun findUserByUsername(username: String): User? = suspendTransaction {
        UserEntity.find { UsersTable.username eq username }.firstOrNull()?.toModel()
    }

    override suspend fun createUser(username: String, password: Argon2): User = suspendTransaction {
        UserEntity.new {
            this.username = username
            this.passwordHash = password
        }.toModel()
    }
}
