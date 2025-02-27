package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable
import io.github.nitkc_proken.freight.backend.entity.ContainerEntity
import io.github.nitkc_proken.freight.backend.values.DockerId
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.NetworkInterfaceName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

interface ContainerRepository {
    suspend fun createContainer(
        networkId: EntityID<Uuid>,
        ipAddress: IPv4Address,
    ): Container?

    suspend fun updateNICNames(
        containerEntity: ContainerEntity,
        vEthNetworkInterfaceName: NetworkInterfaceName,
        vEthContainerInterfaceName: NetworkInterfaceName,
    ): ContainerEntity
}


@Serializable
data class Container(
    val id: Uuid,
    val containerId: DockerId,
    val network: Network,
    val ipAddress: IPv4Address,
    val hostVEthName: NetworkInterfaceName?,
    val containerVEthName: NetworkInterfaceName?,
    val numericId: UInt
) : Model<Uuid> {
    companion object : EntityToModel<ContainerEntity, Container> {
        override fun ContainerEntity.toModel(): Container = Container(
            id.value,
            containerId,
            network.toModel(),
            ipAddress,
            hostVEthName,
            containerVEthName,
            numericId
        )
    }


    val vEthHostInterfaceNameCandidate get() = NetworkInterfaceName.generateNICName(numericId, suffix = "-veth")
    val vEthContainerInterfaceNameCandidate get() = NetworkInterfaceName.generateNICName(numericId, suffix = "-veth-c")
    override fun toEntityId(): EntityID<Uuid> =
        EntityID(id, ContainersTable)
}