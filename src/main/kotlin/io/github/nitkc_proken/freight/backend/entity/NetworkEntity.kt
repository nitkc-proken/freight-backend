package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntity
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

class NetworkEntity(id: EntityID<Uuid>) : KUUIDEntity(id) {
    companion object : KUUIDEntityClass<NetworkEntity>(NetworksTable)

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
