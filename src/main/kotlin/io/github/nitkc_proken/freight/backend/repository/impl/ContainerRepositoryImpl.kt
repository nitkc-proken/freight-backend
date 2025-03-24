package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.entity.ContainerEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.repository.Container
import io.github.nitkc_proken.freight.backend.repository.Container.Companion.toModel
import io.github.nitkc_proken.freight.backend.repository.Container.Companion.toModels
import io.github.nitkc_proken.freight.backend.repository.ContainerRepository
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class ContainerRepositoryImpl : ContainerRepository {

    override suspend fun list(): List<Container> = suspendTransaction {
        ContainerEntity.all().toModels()
    }

    override suspend fun get(id: Uuid): Container? = suspendTransaction {
        ContainerEntity.findById(id)?.toModel()
    }

    override suspend fun create(t: Container): Container = suspendTransaction {
        ContainerEntity.new(t.id) {
            this.containerId = t.containerId
            this.network = NetworkEntity[t.network.toEntityId()]
            this.ipAddress = t.ipAddress
            this.hostVEthName = t.hostVEthName
            this.containerVEthName = t.containerVEthName
        }.toModel()
    }

    override suspend fun update(t: Container): Container = suspendTransaction {
        ContainerEntity[t.id].apply {
            containerId = t.containerId
            network = NetworkEntity[t.network.toEntityId()]
            ipAddress = t.ipAddress
            hostVEthName = t.hostVEthName
            containerVEthName = t.containerVEthName
        }.toModel()
    }

    override suspend fun delete(id: Uuid): Unit = suspendTransaction {
        ContainerEntity[id].delete()
    }

    /*override suspend fun updateNICNames(
        containerEntity: ContainerEntity,
        vEthNetworkInterfaceName: NetworkInterfaceName,
        vEthContainerInterfaceName: NetworkInterfaceName,
    ): ContainerEntity = suspendTransaction {
        containerEntity.apply {
            this.hostVEthName = vEthNetworkInterfaceName
            this.containerVEthName = vEthContainerInterfaceName
        }
    }

    override suspend fun createContainer(networkId: EntityID<Uuid>, ipAddress: IPv4Address): Container? =
        suspendTransaction {
            val container = dockerClient.createContainerCmd(CONTAINER_IMAGE)
                .withHostConfig(DefaultHostConfig).withTty(true).withAttachStdin(true).exec()
            val networkEntity = NetworkEntity[networkId]
            val containerEntity = ContainerEntity.new {
                this.containerId = DockerId(container.id)
                this.network = networkEntity
                this.ipAddress = ipAddress
            }
            commit()
            val containerModel = containerEntity.toModel()

            dockerClient.startContainerCmd(container.id).exec()
            val networkModel = networkEntity.toModel()
            val nsPath = dockerClient.inspectContainerCmd(container.id).exec().networkSettings.sandboxKey
            gatewayClient.initiateContainerNetwork(
                networkModel,
                networkModel.bridgeInterfaceName!!,
                containerModel.vEthHostInterfaceNameCandidate,
                containerModel.vEthContainerInterfaceNameCandidate,
                nsPath.toString(),
                ipAddress
            )
            containerEntity.apply {
                this.hostVEthName = containerModel.vEthHostInterfaceNameCandidate
                this.containerVEthName = containerModel.vEthContainerInterfaceNameCandidate
            }.toModel()
            /*updateNICNames(
                containerEntity,
                networkModel.vEthInterfaceNameCandidate,
                networkModel.vEthContainerInterfaceNameCandidate
            )*/
        }*/
}
