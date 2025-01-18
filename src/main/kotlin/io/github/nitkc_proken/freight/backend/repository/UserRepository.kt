@file:OptIn(ExperimentalUuidApi::class)

package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.values.Argon2
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid


interface UserRepository {
    suspend fun findUserById(id: UUID): User?
    suspend fun findUserByUsername(username: String): User?
    suspend fun createUser(username: String, password: Argon2): User
}


@Serializable
data class User(
    val id: Uuid,
    val username: String,
    val passwordHash: Argon2
)


@OptIn(ExperimentalUuidApi::class)
fun UserEntity.toModel(): User = User(id.value.toKotlinUuid(), username, passwordHash)

@OptIn(ExperimentalUuidApi::class)
fun User.toEntity(): UserEntity = UserEntity(EntityID(id.toJavaUuid(), UsersTable))