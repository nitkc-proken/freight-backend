@file:OptIn(ExperimentalUuidApi::class)

package io.github.nitkc_proken.freight.backend.feature.users

import io.github.nitkc_proken.freight.backend.repository.User
import kotlinx.serialization.Serializable
import org.koin.core.annotation.Single
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Single
class UserService {
    fun getUserSelf(user: User): UserResponse {
        return user.toResponse()
    }
}

@OptIn(ExperimentalUuidApi::class)
fun User.toResponse() = UserResponse(
    id = id,
    username = username,
)

@Serializable
data class UserResponse(
    val id: Uuid,
    val username: String,
)
