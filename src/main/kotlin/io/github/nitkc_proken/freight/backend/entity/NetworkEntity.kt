package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class NetworkEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<NetworkEntity>(NetworksTable)

    val numericId by NetworksTable.numericId

    var name by NetworksTable.name
    var networkAddr by NetworksTable.networkAddr
    var containersNetworkAddr by NetworksTable.containersNetworkAddr
    var clientsNetworkAddr by NetworksTable.clientNetworkAddr
    var dockerNetworkId by NetworksTable.dockerNetworkId
    var tunInterfaceName by NetworksTable.tunInterfaceName
    var vrfInterfaceName by NetworksTable.vrfInterfaceName
    var bridgeInterfaceName by NetworksTable.bridgeInterfaceName

    var owner by UserEntity referencedOn NetworksTable.owner

    val members by NetworkMemberEntity referrersOn NetworkMembersTable.network
}
