package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkMemberEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.values.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

interface NetworkRepository {
    suspend fun createNetwork(
        name: String,
        networkAddressWithMask: NetworkAddressWithMask,
        containerNetworkAddressWithMask: NetworkAddressWithMask,
        clientNetworkAddressWithMask: NetworkAddressWithMask,
        owner: EntityID<Uuid>,
    ): Network

    suspend fun updateNICNames(
        network: EntityID<Uuid>,
        tunInterfaceName: NetworkInterfaceName,
        vrfInterfaceName: NetworkInterfaceName,
        bridgeInterfaceName: NetworkInterfaceName,
    ): Network

    suspend fun clearNICNames(network: EntityID<Uuid>): Network

    suspend fun listAllNetworks(): List<Network>

    suspend fun listOwnedNetworks(user: EntityID<Uuid>): List<Network>

    suspend fun getOwnedNetworkByName(user: UserEntity, name: String): Network?

    suspend fun listUsedIPAddress(network: EntityID<Uuid>): List<IPv4Address>
    suspend fun getJoinedNetworkByFullName(user: EntityID<Uuid>, owner: EntityID<Uuid>, name: String): Network?
    suspend fun getJoinedNetworkById(user: EntityID<Uuid>, network: EntityID<Uuid>): Network?
}

@Serializable
data class Network constructor(
    val id: Uuid,
    val numericId: UInt,
    val name: String,
    val networkAddressWithMask: NetworkAddressWithMask,
    val containersNetworkAddressWithMask: NetworkAddressWithMask,
    val clientsNetworkAddressWithMask: NetworkAddressWithMask,
    val owner: User,
    val members: List<NetworkMember>,

    val dockerNetworkId: DockerId?,
    val tunInterfaceName: NetworkInterfaceName?,
    val vrfInterfaceName: NetworkInterfaceName?,
    val bridgeInterfaceName: NetworkInterfaceName?,
) : Model<Uuid> {

    companion object : EntityToModel<NetworkEntity, Network> {
        override fun NetworkEntity.toModel(): Network = Network(
            id.value,
            numericId,
            name,
            networkAddr,
            containersNetworkAddr,
            clientsNetworkAddr,
            owner.toModel(),
            members.map { it.toModel() },
            dockerNetworkId,
            tunInterfaceName,
            vrfInterfaceName,
            bridgeInterfaceName,
        )
    }

    val tunInterfaceNameCandidate get() = NetworkInterfaceName.generateNICName(numericId, suffix = "-tun")
    val vrfInterfaceNameCandidate get() = NetworkInterfaceName.generateNICName(numericId, suffix = "-vrf")
    val bridgeInterfaceNameCandidate get() = NetworkInterfaceName.generateNICName(numericId, suffix = "-br")

    val vrfRouteNumber get() = numericId + 1000u
    override fun toEntityId(): EntityID<Uuid> = EntityID(id, NetworksTable)
}

@Serializable
data class NetworkMember(
    val user: User,
    val permission: Permissions
)

fun NetworkEntity.toModel(): Network = Network(
    id.value,
    numericId,
    name,
    networkAddr,
    containersNetworkAddr,
    clientsNetworkAddr,
    owner.toModel(),
    members.map { it.toModel() },
    dockerNetworkId,
    tunInterfaceName,
    vrfInterfaceName,
    bridgeInterfaceName,
)

fun NetworkMemberEntity.toModel(): NetworkMember = NetworkMember(
    member.toModel(),
    permission
)
