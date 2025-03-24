package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.entity.TunnelSessionEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.TunnelSession
import io.github.nitkc_proken.freight.backend.repository.TunnelSession.Companion.toModel
import io.github.nitkc_proken.freight.backend.repository.TunnelSessionRepository
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import org.jetbrains.exposed.dao.id.EntityID
import org.koin.core.annotation.Single
import java.util.*
import kotlin.uuid.Uuid

@Single
class TunnelSessionRepositoryImpl : TunnelSessionRepository {
    override suspend fun createTunnelSession(
        user: EntityID<Uuid>,
        network: EntityID<Uuid>,
        ipAddress: IPv4Address
    ): TunnelSession = suspendTransaction {
        TunnelSessionEntity.new {
            this.user = UserEntity[user]
            this.network = NetworkEntity[network]
            this.clientIp = ipAddress
        }.toModel()
    }
}
