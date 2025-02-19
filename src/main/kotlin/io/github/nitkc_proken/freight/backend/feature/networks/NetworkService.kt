package io.github.nitkc_proken.freight.backend.feature.networks

import io.github.nitkc_proken.freight.backend.feature.networks.model.CreateNetworkRequest
import io.github.nitkc_proken.freight.backend.grpc.gatewayClient
import io.github.nitkc_proken.freight.backend.repository.*
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.NetworkAddressWithMask
import io.github.nitkc_proken.freight.backend.values.NetworkInterfaceName
import org.koin.core.annotation.Single

@Single
class NetworkService(
    val networkRepository: NetworkRepository,
    val userRepository: UserRepository,
) {
    suspend fun createNetwork(user: User, createNetworkRequest: CreateNetworkRequest): Network {
        val network = networkRepository.createNetwork(
            owner = user.toEntityId(),
            name = createNetworkRequest.name,
            networkAddressWithMask = createNetworkRequest.networkAddress,
            containerNetworkAddressWithMask = createNetworkRequest.containerAddress,
            clientNetworkAddressWithMask = createNetworkRequest.clientAddress
        )
        return initNetwork(network)
    }

    suspend fun initNetwork(network: Network): Network {
        val res = gatewayClient.initiateNetwork(
            network,
            network.tunInterfaceNameCandidate,
            network.vrfInterfaceNameCandidate,
            network.bridgeInterfaceNameCandidate
        )
        return networkRepository
            .updateNICNames(
                network.toEntityId(),
                NetworkInterfaceName(res.tunInterfaceName),
                NetworkInterfaceName(res.vrfInterfaceName),
                NetworkInterfaceName(res.bridgeInterfaceName)
            )
    }

    suspend fun cleanUpNetwork(network: Network): Network {
        gatewayClient.cleanUpNetwork(network)
        return networkRepository.clearNICNames(
            network.toEntityId(),
        )
    }

    suspend fun getNetworks(user: User): List<Network> {
        return networkRepository.listOwnedNetworks(user.toEntityId())
    }

    suspend fun getJoinedNetworkByOwnerAndName(user: User, ownerName: String, name: String): Network? {
        val owner = userRepository.findUserByUsername(ownerName) ?: return null
        return networkRepository.getJoinedNetworkByFullName(user.toEntityId(), owner.toEntityId(), name)
    }

    suspend fun getContainerEmptyIPAddress(network: Network): IPv4Address? {
        val usedIPs = networkRepository.listUsedIPAddress(network.toEntityId())
        return getEmptyIPAddressBySubnet(network.containersNetworkAddressWithMask, usedIPs)
    }

    suspend fun getClientEmptyIPAddress(network: Network): IPv4Address? {
        val usedIPs = networkRepository.listUsedIPAddress(network.toEntityId())
        return getEmptyIPAddressBySubnet(network.clientsNetworkAddressWithMask, usedIPs)
    }

    private fun getEmptyIPAddressBySubnet(
        subnet: NetworkAddressWithMask,
        usedIPs: List<IPv4Address>
    ): IPv4Address? = subnet.availableIPAddress().firstOrNull {
        it !in usedIPs
    }
}