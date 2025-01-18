package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.TunnelSessionsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class TunnelSessionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TunnelSessionEntity>(TunnelSessionsTable)
    var tunnel by TunnelSessionsTable.network
    var user by TunnelSessionsTable.user
    var clientIp by TunnelSessionsTable.clientIp
}
