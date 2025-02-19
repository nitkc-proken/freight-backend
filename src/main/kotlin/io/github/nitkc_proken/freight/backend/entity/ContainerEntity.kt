package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class ContainerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ContainerEntity>(ContainersTable)

    var containerId by ContainersTable.containerId
    var network by NetworkEntity referencedOn ContainersTable.network
    var ipAddress by ContainersTable.ipAddress
    var hostVEthName by ContainersTable.hostVEthName
    var containerVEthName by ContainersTable.containerVEthName
    val numericId by ContainersTable.numericId
}