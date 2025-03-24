package io.github.nitkc_proken.freight.backend.database.tables

import com.google.protobuf.enum
import io.github.nitkc_proken.freight.backend.database.columntype.enum
import io.github.nitkc_proken.freight.backend.values.Permissions
import org.jetbrains.exposed.dao.id.CompositeIdTable

object NetworkMembersTable : CompositeIdTable() {
    val user = reference("user", UsersTable)
    val network = reference("network", NetworksTable)
    val permission = enum<Permissions>("permission")
    override val primaryKey = PrimaryKey(user, network)
    init {
        addIdColumn(user)
        addIdColumn(network)
    }
}
