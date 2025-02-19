package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable.containerId
import io.github.nitkc_proken.freight.backend.database.tables.TunnelSessionsTable
import io.github.nitkc_proken.freight.backend.entity.TunnelSessionEntity
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

interface TunnelSessionRepository {
    suspend fun createTunnelSession(
        user: EntityID<UUID>,
        network: EntityID<UUID>,
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
) : Model<UUID> {
    companion object : EntityToModel<TunnelSessionEntity, TunnelSession> {
        override fun TunnelSessionEntity.toModel(): TunnelSession {
            return TunnelSession(
                id.value.toKotlinUuid(),
                user.toModel(),
                network.toModel(),
                clientIp
            )
        }
    }

    override fun toEntityId(): EntityID<UUID> {
        return EntityID(id.toJavaUuid(), TunnelSessionsTable)
    }

}
