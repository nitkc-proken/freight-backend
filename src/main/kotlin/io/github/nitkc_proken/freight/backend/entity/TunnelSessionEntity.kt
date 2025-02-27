package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.TunnelSessionsTable
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntity
import io.github.nitkc_proken.freight.backend.database.tabletype.KUUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

class TunnelSessionEntity(id: EntityID<Uuid>) : KUUIDEntity(id) {
    companion object : KUUIDEntityClass<TunnelSessionEntity>(TunnelSessionsTable)
    var network by NetworkEntity referencedOn TunnelSessionsTable.network
    var user by UserEntity referencedOn TunnelSessionsTable.user
    var clientIp by TunnelSessionsTable.clientIp
}
