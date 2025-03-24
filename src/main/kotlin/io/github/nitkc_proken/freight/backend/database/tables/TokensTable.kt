package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import kotlin.uuid.Uuid

object TokensTable : KUUIDTable() {
    val token = varchar("token", 256).clientDefault { Uuid.random().toString() }.uniqueIndex()
    val user = reference("user", UsersTable)
    val expiresAt = timestamp("expires_at")
}
