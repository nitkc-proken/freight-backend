package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TokenEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, TokenEntity>(TokensTable)

    var token by TokensTable.id
    var user by UserEntity referencedOn TokensTable.user
    var expiresAt by TokensTable.expiresAt
}
