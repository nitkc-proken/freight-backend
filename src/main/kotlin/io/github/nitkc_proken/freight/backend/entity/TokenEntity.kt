package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntity
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

class TokenEntity(id: EntityID<Uuid>) : KUUIDEntity(id) {
    companion object : KUUIDEntityClass<TokenEntity>(TokensTable)

    val token by TokensTable.token
    var user by UserEntity referencedOn TokensTable.user
    var expiresAt by TokensTable.expiresAt
}
