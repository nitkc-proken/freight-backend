package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.ipv4Address
import org.jetbrains.exposed.dao.id.UUIDTable

object TunnelSessionsTable : UUIDTable() {
    val network = reference("network", NetworksTable)
    val clientIp = ipv4Address("client_ip")
    val user = reference("user", UsersTable)
}
