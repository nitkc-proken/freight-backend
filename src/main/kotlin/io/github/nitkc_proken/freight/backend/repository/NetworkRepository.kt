package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkMemberEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.values.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

interface NetworkRepository {
    suspend fun createNetwork(
        name: String,
        networkAddressWithMask: NetworkAddressWithMask,
        containerNetworkAddressWithMask: NetworkAddressWithMask,
        clientNetworkAddressWithMask: NetworkAddressWithMask,
        owner: EntityID<UUID>,
    ): Network

    suspend fun updateNICNames(
        network: EntityID<UUID>,
        tunInterfaceName: NetworkInterfaceName,
        vrfInterfaceName: NetworkInterfaceName,
        bridgeInterfaceName: NetworkInterfaceName,
    ): Network

    suspend fun clearNICNames(network: EntityID<UUID>): Network

    suspend fun listAllNetworks(): List<Network>

    suspend fun listOwnedNetworks(user: EntityID<UUID>): List<Network>

    suspend fun getOwnedNetworkByName(user: UserEntity, name: String): Network?

    suspend fun listUsedIPAddress(network: EntityID<UUID>): List<IPv4Address>
    suspend fun getJoinedNetworkByFullName(user: EntityID<UUID>, owner: EntityID<UUID>, name: String): Network?
    suspend fun getJoinedNetworkById(user: EntityID<UUID>, network: EntityID<UUID>): Network?
}

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class Network @OptIn(ExperimentalUuidApi::class) constructor(
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
) : Model<UUID> {

    companion object : EntityToModel<NetworkEntity, Network> {
        @OptIn(ExperimentalUuidApi::class)
        override fun NetworkEntity.toModel(): Network = Network(
            id.value.toKotlinUuid(),
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
    override fun toEntityId(): EntityID<UUID> = EntityID(id.toJavaUuid(), NetworksTable)
}

@Serializable
data class NetworkMember(
    val user: User,
    val permission: Permissions
)

@OptIn(ExperimentalUuidApi::class)
fun NetworkEntity.toModel(): Network = Network(
    id.value.toKotlinUuid(),
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
    member.toModel(), permission
)