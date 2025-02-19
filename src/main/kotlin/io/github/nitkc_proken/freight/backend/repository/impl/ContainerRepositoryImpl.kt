package io.github.nitkc_proken.freight.backend.repository.impl

import com.github.dockerjava.api.model.HostConfig
import io.github.nitkc_proken.freight.backend.entity.ContainerEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.grpc.gatewayClient
import io.github.nitkc_proken.freight.backend.repository.Container.Companion.toModel
import io.github.nitkc_proken.freight.backend.repository.ContainerRepository
import io.github.nitkc_proken.freight.backend.repository.toModel
import io.github.nitkc_proken.freight.backend.utils.dockerClient
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import io.github.nitkc_proken.freight.backend.values.DockerId
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.NetworkInterfaceName
import org.jetbrains.exposed.dao.id.EntityID
import org.koin.core.annotation.Single
import java.util.*

@Single
class ContainerRepositoryImpl : ContainerRepository {

    companion object {
        private const val CONTAINER_IMAGE = "nicolaka/netshoot"
        val DefaultHostConfig = HostConfig.newHostConfig()
            .withNetworkMode("none")
    }

    override suspend fun updateNICNames(
        containerEntity: ContainerEntity,
        vEthNetworkInterfaceName: NetworkInterfaceName,
        vEthContainerInterfaceName: NetworkInterfaceName,
    ): ContainerEntity = suspendTransaction {
        containerEntity.apply {
            this.hostVEthName = vEthNetworkInterfaceName
            this.containerVEthName = vEthContainerInterfaceName
        }
    }

    override suspend fun createContainer(networkId: EntityID<UUID>, ipAddress: IPv4Address): ContainerEntity =
        suspendTransaction {
            val container = dockerClient.createContainerCmd(CONTAINER_IMAGE)
                .withHostConfig(DefaultHostConfig).withTty(true).withAttachStdin(true).exec()
            val networkEntity = NetworkEntity[networkId]
            val containerEntity = ContainerEntity.new {
                this.containerId = DockerId(container.id)
                this.network = networkEntity
                this.ipAddress = ipAddress
            }
            val containerModel = containerEntity.toModel()
            commit()

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
            }
            /*updateNICNames(
                containerEntity,
                networkModel.vEthInterfaceNameCandidate,
                networkModel.vEthContainerInterfaceNameCandidate
            )*/
        }
}