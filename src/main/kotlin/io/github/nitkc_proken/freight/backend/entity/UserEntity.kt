package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntity
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

class UserEntity(id: EntityID<Uuid>) : KUUIDEntity(id) {
    companion object : KUUIDEntityClass<UserEntity>(UsersTable)

    var username by UsersTable.username
    var passwordHash by UsersTable.passwordHash

    val networks by NetworkMemberEntity referrersOn NetworkMembersTable.user
    val tokens by TokenEntity referrersOn TokensTable.user
}
