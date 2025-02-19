package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TokensTable : KUUIDTable() {
    val user = reference("user", UsersTable)
    val expiresAt = timestamp("expires_at")
}
