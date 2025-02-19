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
    suspend fun getUserWithPasswordHash(username: String): Pair<User,Argon2>?
    suspend fun createUser(username: String, password: Argon2): User
}

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class User(
    val id: Uuid,
    val username: String,
) : Model<UUID> {
    companion object : EntityToModel<UserEntity, User> {
        override fun UserEntity.toModel(): User = User(
            id.value.toKotlinUuid(), username,
        )
    }

    override fun toEntityId(): EntityID<UUID> = EntityID(id.toJavaUuid(), UsersTable)

}



