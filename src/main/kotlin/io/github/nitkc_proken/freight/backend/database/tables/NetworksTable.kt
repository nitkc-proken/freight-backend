package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.dockerId
import io.github.nitkc_proken.freight.backend.database.columntype.ipv4Address
import io.github.nitkc_proken.freight.backend.database.columntype.networkInterfaceName
import io.github.nitkc_proken.freight.backend.database.columntype.subnetMaskLength
import org.jetbrains.exposed.dao.id.UUIDTable

object NetworksTable : UUIDTable() {
    val name = varchar("name", 50)
    val address = ipv4Address("address")
    val subnetMaskLength = subnetMaskLength("subnetmask_length")
    val dockerNetworkId = dockerId("docker_network_id").nullable()
    val tap_interface_name = networkInterfaceName("tap_interface_name").nullable()
    val docker_interface_name = networkInterfaceName("docker_interface_name").nullable()
}
