package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.values.Argon2
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserRepository {
    suspend fun findUserById(id: Uuid): User?
    suspend fun findUserByUsername(username: String): User?
    suspend fun getUserWithPasswordHash(username: String): Pair<User,Argon2>?
    suspend fun createUser(username: String, password: Argon2): User
}

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class User(
    val id: Uuid,
    val username: String,
) : Model<Uuid> {
    companion object : EntityToModel<UserEntity, User> {
        override fun UserEntity.toModel(): User = User(
            id.value, username,
        )
    }

    override fun toEntityId(): EntityID<Uuid> = EntityID(id, UsersTable)

}



