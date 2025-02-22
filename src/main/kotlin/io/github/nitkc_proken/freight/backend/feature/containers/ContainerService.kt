package io.github.nitkc_proken.freight.backend.feature.containers

import io.github.nitkc_proken.freight.backend.feature.containers.model.CreateContainerRequest
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.repository.Container.Companion.toModel
import org.koin.core.annotation.Single

@Single
class ContainerService(

    private val containerRepository: ContainerRepository,
    private val userRepository: UserRepository,
    private val networkRepository: NetworkRepository
) {
    suspend fun createContainer(user: User, request: CreateContainerRequest): Container? {
        val (ownerName, networkName) = request.split
        val owner = userRepository.findUserByUsername(ownerName)!!
        val network = networkRepository.getJoinedNetworkByFullName(user.toEntityId(), owner.toEntityId(), networkName)!!
        val usedIpAddress = networkRepository.listUsedIPAddress(network.toEntityId())
        val nwModel = network
        val newIpAddress = nwModel.containersNetworkAddressWithMask.availableIPAddress().firstOrNull {
            it !in usedIpAddress
        }
        return containerRepository.createContainer(network.toEntityId(), newIpAddress!!)
    }
}