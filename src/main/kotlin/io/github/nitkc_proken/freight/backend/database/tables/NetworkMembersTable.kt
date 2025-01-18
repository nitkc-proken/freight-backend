package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.enum
import io.github.nitkc_proken.freight.backend.values.Permissions
import org.jetbrains.exposed.sql.Table

object NetworkMembersTable : Table() {
    val user = reference("user", UsersTable)
    val network = reference("network", NetworksTable)
    val permission = enum<Permissions>("permission")
    override val primaryKey = PrimaryKey(user, network)
}
