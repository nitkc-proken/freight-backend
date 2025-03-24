package io.github.nitkc_proken.freight.backend.feature.containers

import io.github.nitkc_proken.freight.backend.feature.containers.model.CreateContainerRequest
import io.github.nitkc_proken.freight.backend.grpc.gatewayClient
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Single

@Single
class ContainerService(

    private val containerRepository: ContainerRepository,
    private val userRepository: UserRepository,
    private val networkRepository: NetworkRepository,
    private val dockerRepository: DockerRepository
) {
    suspend fun createContainer(user: User, request: CreateContainerRequest): Container? {
        val (ownerName, networkName) = request.split
        val owner = userRepository.findUserByUsername(ownerName)!!
        val network = networkRepository.getJoinedNetworkByFullName(user.toEntityId(), owner.toEntityId(), networkName)!!
        val newIpAddress = getAvailableIPAddress(network) ?: return null

        val containerId = dockerRepository.createContainer(CONTAINER_IMAGE)
        val container =
            containerRepository.create(
                Container(
                    containerId = containerId,
                    network = network,
                    ipAddress = newIpAddress
                )
            )

        dockerRepository.startContainer(containerId)

        val (nsPath) = dockerRepository.inspectContainer(containerId)
        gatewayClient.initiateContainerNetwork(
            network,
            network.bridgeInterfaceName!!,
            container.vEthHostInterfaceNameCandidate,
            container.vEthContainerInterfaceNameCandidate,
            nsPath,
            newIpAddress
        )
        return containerRepository
            .update(
                container.copy(
                    hostVEthName = container.vEthHostInterfaceNameCandidate,
                    containerVEthName = container.vEthContainerInterfaceNameCandidate
                )
            )
        //containerRepository.createContainer(network.toEntityId(), newIpAddress!!)
    }

    // てかこれContainerではなくNetworkのメソッドにしたほうがいいかも
    // TODO: IPAddressでテーブルを作り Reserved/Used/Availableを管理する
    // FIXME: 不完全な排他制御
    private val mutex = Mutex()
    private suspend fun getAvailableIPAddress(network: Network): IPv4Address? = mutex.withLock {
        val usedIpAddress = networkRepository.listUsedIPAddress(network.toEntityId())
        return network.containersNetworkAddressWithMask.availableIPAddress().firstOrNull {
            it !in usedIpAddress
        }
    }
}

// TODO
private const val CONTAINER_IMAGE = "nicolaka/netshoot"