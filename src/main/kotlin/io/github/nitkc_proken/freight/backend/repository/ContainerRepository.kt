package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable
import io.github.nitkc_proken.freight.backend.entity.ContainerEntity
import io.github.nitkc_proken.freight.backend.values.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import kotlin.uuid.Uuid

interface ContainerRepository : Repository<Container, Uuid> {
    /*    suspend fun createContainer(
            networkId: EntityID<Uuid>,
            ipAddress: IPv4Address,
        ): Container?

        suspend fun updateNICNames(
            containerEntity: ContainerEntity,
            vEthNetworkInterfaceName: NetworkInterfaceName,
            vEthContainerInterfaceName: NetworkInterfaceName,
        ): ContainerEntity*/
}

@Serializable
data class Container(
    val id: Uuid = Uuid.random(),
    val containerId: DockerId,
    val network: Network,
    val ipAddress: IPv4Address,
    val hostVEthName: NetworkInterfaceName? = null,
    val containerVEthName: NetworkInterfaceName? = null,
    val shortId: NanoId = ContainerNanoIdGenerator.random(),
) : Model<Uuid> {

    companion object : EntityToModel<ContainerEntity, Container> {
        private val ContainerNanoIdGenerator =
            NanoIdGenerator(alphabet = NetworkInterfaceName.NIC_NAME_CHARACTERS, size = 11)

        override fun ContainerEntity.toModel(): Container = Container(
            id.value,
            containerId,
            network.toModel(),
            ipAddress,
            hostVEthName,
            containerVEthName,
            shortId
        )

    }

    val vEthHostInterfaceNameCandidate get() = NetworkInterfaceName("fvh-$shortId")
    val vEthContainerInterfaceNameCandidate get() = NetworkInterfaceName("fvc-$shortId")
    override fun toEntityId(): EntityID<Uuid> =
        EntityID(id, ContainersTable)
}
