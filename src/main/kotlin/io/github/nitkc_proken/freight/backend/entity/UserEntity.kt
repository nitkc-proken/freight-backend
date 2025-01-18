package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)
    var username by UsersTable.username
    var passwordHash by UsersTable.passwordHash

    val networks by NetworkEntity via NetworkMembersTable
    val tokens by TokenEntity referrersOn TokensTable.user
}