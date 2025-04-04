package io.github.nitkc_proken.freight.backend.database.tables

import io.github.nitkc_proken.freight.backend.database.columntype.dockerId
import io.github.nitkc_proken.freight.backend.database.columntype.ipv4Address
import io.github.nitkc_proken.freight.backend.database.columntype.nanoid
import io.github.nitkc_proken.freight.backend.database.columntype.networkInterfaceName
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDTable

object ContainersTable : KUUIDTable() {
    val containerId = dockerId("container_id")
    val network = reference("network", NetworksTable)
    val ipAddress = ipv4Address("ip_address")
    val hostVEthName = networkInterfaceName("host_veth_name").nullable()
    val containerVEthName = networkInterfaceName("container_veth_name").nullable()

    // fvh-<short_id> →
    // fvc-<short_id> → 15 chars
    val shortId = nanoid("short_id", 11).uniqueIndex()
}
