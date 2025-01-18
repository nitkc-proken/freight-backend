package io.github.nitkc_proken.freight.backend.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object TokensTable : IdTable<String>() {
    @OptIn(ExperimentalUuidApi::class)
    override val id = varchar("id", 128)
        .clientDefault {
            Uuid.random().toString()
        }.entityId()
    val user = reference("user", UsersTable)
    val expiresAt = timestamp("expires_at")
}
