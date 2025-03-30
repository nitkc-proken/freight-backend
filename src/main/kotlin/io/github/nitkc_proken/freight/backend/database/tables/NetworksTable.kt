package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.*
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDTable

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

    // ftun-<short_id>
    // fvrf-<short_id>
    // fbr-<short_id> → 15 chars
    val shortId = nanoid("short_id", 10).uniqueIndex()
    val vrfRouteTableId = integer("vrf_route_table_id").nullable()
    init {
        uniqueIndex("full_name", name, owner)
    }
}
