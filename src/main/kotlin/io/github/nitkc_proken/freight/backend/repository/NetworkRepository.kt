package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkMemberEntity
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.repository.User.Companion.toModel
import io.github.nitkc_proken.freight.backend.values.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

interface NetworkRepository : Repository<Network, Uuid> {

    suspend fun listOwnedNetworks(user: EntityID<Uuid>): List<Network>

    suspend fun getOwnedNetworkByName(user: UserEntity, name: String): Network?

    suspend fun listUsedIPAddress(network: EntityID<Uuid>): List<IPv4Address>
    suspend fun getJoinedNetworkByFullName(user: EntityID<Uuid>, owner: EntityID<Uuid>, name: String): Network?
    suspend fun getJoinedNetworkById(user: EntityID<Uuid>, network: EntityID<Uuid>): Network?
}

@Serializable
data class Network(
    val id: Uuid = Uuid.random(),
    val name: String,
    val networkAddressWithMask: NetworkAddressWithMask,
    val containersNetworkAddressWithMask: NetworkAddressWithMask,
    val clientsNetworkAddressWithMask: NetworkAddressWithMask,
    val owner: User,
    val members: List<NetworkMember> = listOf(
        NetworkMember(owner, Permissions.Owner)
    ),

    val dockerNetworkId: DockerId? = null,
    val tunInterfaceName: NetworkInterfaceName? = null,
    val vrfInterfaceName: NetworkInterfaceName? = null,
    val bridgeInterfaceName: NetworkInterfaceName? = null,
    val shortId: NanoId = NetworkNanoIdGenerator.random(),
    val vrfRouteTableId: Int? = null,
) : Model<Uuid> {

    companion object : EntityToModel<NetworkEntity, Network> {
        private val NetworkNanoIdGenerator =
            NanoIdGenerator(alphabet = NetworkInterfaceName.NIC_NAME_CHARACTERS, size = 10)

        override fun NetworkEntity.toModel(): Network = Network(
            id.value,
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
            shortId,
            vrfRouteTableId,
        )
    }

    val tunInterfaceNameCandidate get() = NetworkInterfaceName("ftun-$shortId")
    val vrfInterfaceNameCandidate get() = NetworkInterfaceName("fvrf-$shortId")
    val bridgeInterfaceNameCandidate get() = NetworkInterfaceName("fbr-$shortId")

    override fun toEntityId(): EntityID<Uuid> = EntityID(id, NetworksTable)
}

@Serializable
data class NetworkMember(
    val user: User,
    val permission: Permissions
)


fun NetworkMemberEntity.toModel(): NetworkMember = NetworkMember(
    member.toModel(),
    permission
)
