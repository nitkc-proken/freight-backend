package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.database.tables.*
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkMemberEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.Network
import io.github.nitkc_proken.freight.backend.repository.NetworkRepository
import io.github.nitkc_proken.freight.backend.repository.toModel
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.NetworkAddressWithMask
import io.github.nitkc_proken.freight.backend.values.NetworkInterfaceName
import io.github.nitkc_proken.freight.backend.values.Permissions
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.alias
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.union
import org.koin.core.annotation.Single
import java.util.*
import kotlin.uuid.Uuid

@Single
class NetworkRepositoryImpl : NetworkRepository {
    override suspend fun createNetwork(
        name: String,
        networkAddressWithMask: NetworkAddressWithMask,
        containerNetworkAddressWithMask: NetworkAddressWithMask,
        clientNetworkAddressWithMask: NetworkAddressWithMask,
        owner: EntityID<Uuid>
    ): Network = suspendTransaction {
        val entity = NetworkEntity.new {
            this.name = name
            this.networkAddr = networkAddressWithMask
            this.containersNetworkAddr = containerNetworkAddressWithMask
            this.clientsNetworkAddr = clientNetworkAddressWithMask
            this.owner = UserEntity[owner]
        }.also {
            NetworkMemberEntity.new {
                this.network = it
                this.member = UserEntity[owner]
                this.permission = Permissions.Owner
            }
        }
        commit()
        entity.toModel()
    }

    override suspend fun updateNICNames(
        network: EntityID<Uuid>,
        tunInterfaceName: NetworkInterfaceName,
        vrfInterfaceName: NetworkInterfaceName,
        bridgeInterfaceName: NetworkInterfaceName
    ): Network = suspendTransaction {
        NetworkEntity[network].apply {
            this.tunInterfaceName = tunInterfaceName
            this.vrfInterfaceName = vrfInterfaceName
            this.bridgeInterfaceName = bridgeInterfaceName
        }.toModel()
    }

    override suspend fun clearNICNames(network: EntityID<Uuid>): Network = suspendTransaction {
        NetworkEntity[network].apply {
            this.tunInterfaceName = null
            this.vrfInterfaceName = null
            this.bridgeInterfaceName = null
        }.toModel()
    }

    override suspend fun listAllNetworks(): List<Network> = suspendTransaction {
        NetworkEntity.all().map { it.toModel() }
    }

    override suspend fun listOwnedNetworks(user: EntityID<Uuid>): List<Network> = suspendTransaction {
        NetworkEntity
            .find { NetworksTable.owner eq user }
            .map { it.toModel() }
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
    ): Network? =
        suspendTransaction {
            NetworksTable.innerJoin(NetworkMembersTable)
                .select(NetworksTable.columns)
                .where {
                    (NetworksTable.owner eq owner) and
                        (NetworksTable.name eq name) and
                        (NetworkMembersTable.user eq user)
                }
                .singleOrNull()
                ?.let { NetworkEntity.wrapRow(it).toModel() }
        }

    override suspend fun getJoinedNetworkById(user: EntityID<Uuid>, network: EntityID<Uuid>): Network? =
        suspendTransaction {
            NetworksTable.innerJoin(NetworkMembersTable)
                .select(NetworksTable.columns)
                .where {
                    (NetworksTable.id eq network) and
                        (NetworkMembersTable.user eq user)
                }
                .singleOrNull()
                ?.let { NetworkEntity.wrapRow(it).toModel() }
        }

    override suspend fun listUsedIPAddress(network: EntityID<Uuid>): List<IPv4Address> = suspendTransaction {
        val containerQuery = ContainersTable.select(ContainersTable.ipAddress.alias("ip"))
            .where { ContainersTable.network eq network }
        val tunnelQuery = TunnelSessionsTable.select(TunnelSessionsTable.clientIp.alias("ip"))
            .where { TunnelSessionsTable.network eq network }
        val usedIps = containerQuery.union(tunnelQuery).alias("used_ips")

        val ip = usedIps[ContainersTable.ipAddress.alias("ip")]
        usedIps.select(ip).map { it[ip] }
    }
}
