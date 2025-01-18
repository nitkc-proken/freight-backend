package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class NetworkEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<NetworkEntity>(NetworksTable)

    var name by NetworksTable.name
    var address by NetworksTable.address
    var subnetMaskLength by NetworksTable.subnetMaskLength
    val members by UserEntity via NetworkMembersTable
}
