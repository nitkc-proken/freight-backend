package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.TunnelSessionsTable
import io.github.nitkc_proken.freight.backend.entity.TunnelSessionEntity
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface TunnelSessionRepository {
    suspend fun createTunnelSession(
        user: EntityID<Uuid>,
        network: EntityID<Uuid>,
        ipAddress: IPv4Address,
    ): TunnelSession
}

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class TunnelSession(
    val id: Uuid,
    val user: User,
    val network: Network,
    val ipAddress: IPv4Address,
) : Model<Uuid> {
    companion object : EntityToModel<TunnelSessionEntity, TunnelSession> {
        override fun TunnelSessionEntity.toModel(): TunnelSession {
            return TunnelSession(
                id.value,
                user.toModel(),
                network.toModel(),
                clientIp
            )
        }
    }

    override fun toEntityId(): EntityID<Uuid> {
        return EntityID(id, TunnelSessionsTable)
    }

}
