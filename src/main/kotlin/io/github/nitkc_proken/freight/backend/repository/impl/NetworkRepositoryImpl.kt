package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable
import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import io.github.nitkc_proken.freight.backend.database.tables.TunnelSessionsTable
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkMemberEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.EntityRepository
import io.github.nitkc_proken.freight.backend.repository.Network
import io.github.nitkc_proken.freight.backend.repository.Network.Companion.toModel
import io.github.nitkc_proken.freight.backend.repository.NetworkRepository
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.Permissions
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.union
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class NetworkRepositoryImpl : NetworkRepository,
    EntityRepository<Network, Uuid, NetworkEntity, NetworkEntity.Companion>(
        NetworkEntity,
        Network
    ) {
    override suspend fun create(
        value: Network
    ): Network = suspendTransaction {
        val entity = NetworkEntity.new(value.id) {
            name = value.name
            networkAddr = value.networkAddressWithMask
            containersNetworkAddr = value.containersNetworkAddressWithMask
            clientsNetworkAddr = value.clientsNetworkAddressWithMask
            dockerNetworkId = value.dockerNetworkId
            tunInterfaceName = value.tunInterfaceName
            vrfInterfaceName = value.vrfInterfaceName
            bridgeInterfaceName = value.bridgeInterfaceName
            shortId = value.shortId
            owner = UserEntity[value.owner.id]
            vrfRouteTableId = value.vrfRouteTableId
        }.also {
            NetworkMemberEntity.new {
                this.network = it
                this.member = UserEntity[value.owner.id]
                this.permission = Permissions.Owner
            }
        }
        commit()
        entity.toModel()
    }

    override suspend fun update(value: Network): Network = suspendTransaction {
        NetworkEntity[value.id].apply {
            name = value.name
            networkAddr = value.networkAddressWithMask
            containersNetworkAddr = value.containersNetworkAddressWithMask
            clientsNetworkAddr = value.clientsNetworkAddressWithMask
            dockerNetworkId = value.dockerNetworkId
            tunInterfaceName = value.tunInterfaceName
            vrfInterfaceName = value.vrfInterfaceName
            bridgeInterfaceName = value.bridgeInterfaceName
            shortId = value.shortId
            owner = UserEntity[value.owner.id]
            vrfRouteTableId = value.vrfRouteTableId
            NetworkMembersTable.batchUpsert(value.members) { member ->
                this[NetworkMembersTable.network] = this@apply.id
                this[NetworkMembersTable.user] = member.user.id
                this[NetworkMembersTable.permission] = member.permission
            }
        }.toModel()
    }

    override suspend fun listOwnedNetworks(user: EntityID<Uuid>): List<Network> = suspendTransaction {
        NetworkEntity.find { NetworksTable.owner eq user }.map { it.toModel() }
    }

    override suspend fun getOwnedNetworkByName(user: UserEntity, name: String): Network? = suspendTransaction {
        NetworkEntity.find {
            NetworksTable.owner eq user.id and (NetworksTable.name eq name)
        }.singleOrNull()?.toModel()
    }

    override suspend fun getJoinedNetworkByFullName(
        user: EntityID<Uuid>,
        owner: EntityID<Uuid>,
        name: String
    ): Network? = suspendTransaction {
        NetworksTable.innerJoin(NetworkMembersTable).select(NetworksTable.columns).where {
            (NetworksTable.owner eq owner) and (NetworksTable.name eq name) and (NetworkMembersTable.user eq user)
        }.singleOrNull()?.let { NetworkEntity.wrapRow(it).toModel() }
    }

    override suspend fun getJoinedNetworkById(user: EntityID<Uuid>, network: EntityID<Uuid>): Network? =
        suspendTransaction {
            NetworksTable.innerJoin(NetworkMembersTable).select(NetworksTable.columns).where {
                (NetworksTable.id eq network) and (NetworkMembersTable.user eq user)
            }.singleOrNull()?.let { NetworkEntity.wrapRow(it).toModel() }
        }


    override suspend fun listUsedIPAddress(network: EntityID<Uuid>): List<IPv4Address> = suspendTransaction {
        val containerQuery =
            ContainersTable.select(ContainersTable.ipAddress.alias("ip")).where { ContainersTable.network eq network }
        val tunnelQuery = TunnelSessionsTable.select(TunnelSessionsTable.clientIp.alias("ip"))
            .where { TunnelSessionsTable.network eq network }
        val usedIps = containerQuery.union(tunnelQuery).alias("used_ips")

        val ip = usedIps[ContainersTable.ipAddress.alias("ip")]
        usedIps.select(ip).map { it[ip] }
    }
}
