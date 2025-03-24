package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntity
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

class ContainerEntity(id: EntityID<Uuid>) : KUUIDEntity(id) {
    companion object : KUUIDEntityClass<ContainerEntity>(ContainersTable)

    var containerId by ContainersTable.containerId
    var network by NetworkEntity referencedOn ContainersTable.network
    var ipAddress by ContainersTable.ipAddress
    var hostVEthName by ContainersTable.hostVEthName
    var containerVEthName by ContainersTable.containerVEthName
    var shortId by ContainersTable.shortId
}
