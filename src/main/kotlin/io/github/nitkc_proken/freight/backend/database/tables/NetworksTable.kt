package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.*
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDTable
import org.jetbrains.exposed.dao.id.UUIDTable

object NetworksTable : KUUIDTable() {
    val name = varchar("name", 50)
    val networkAddr = networkAddressWithMask("network_addr")
    val containersNetworkAddr = networkAddressWithMask("containers_network_addr")
    val clientNetworkAddr = networkAddressWithMask("client_network_addr")
    val dockerNetworkId = dockerId("docker_network_id").nullable()
    val tunInterfaceName = networkInterfaceName("tun_interface_name").nullable()
    val vrfInterfaceName = networkInterfaceName("vrf_interface_name").nullable()
    val bridgeInterfaceName = networkInterfaceName("bridge_interface_name").nullable()
    val owner = reference("owner", UsersTable)

    val numericId = uinteger("numeric_id").autoIncrement().uniqueIndex("nw_numeric_id")

    init {
        uniqueIndex("full_name", name, owner)
    }
}
