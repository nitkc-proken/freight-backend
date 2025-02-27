package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.argon2
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDTable
import org.jetbrains.exposed.dao.id.UUIDTable

object UsersTable : KUUIDTable() {
    val username = varchar("username", 50).uniqueIndex()

    // / Bcrypto
    val passwordHash = argon2("password_hash")
}
