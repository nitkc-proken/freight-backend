package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.dockerId
import io.github.nitkc_proken.freight.backend.database.columntype.ipv4Address
import io.github.nitkc_proken.freight.backend.database.columntype.networkInterfaceName
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.feature.networks.Networks
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.statements.DeleteStatement.Companion.where

object ContainersTable : UUIDTable() {
    val containerId = dockerId("container_id")
    val network = reference("network", NetworksTable)
    val ipAddress = ipv4Address("ip_address")
    val hostVEthName = networkInterfaceName("host_veth_name").nullable()
    val containerVEthName = networkInterfaceName("container_veth_name").nullable()
    val numericId = uinteger("numeric_id").autoIncrement().uniqueIndex()
}