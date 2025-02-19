package io.github.nitkc_proken.freight.backend.grpc


import gateway.*
import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable.containerId
import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable.containerVEthName
import io.github.nitkc_proken.freight.backend.database.tables.ContainersTable.hostVEthName
import io.github.nitkc_proken.freight.backend.repository.Network
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.NetworkInterfaceName
import io.github.nitkc_proken.freight.utils.dotenv
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.ktor.utils.io.core.*
import java.util.concurrent.TimeUnit
import kotlin.uuid.ExperimentalUuidApi

val gatewayClient by lazy {
    val host = dotenv["GRPC_GATEWAY_HOST"]
    val port = dotenv["GRPC_GATEWAY_PORT"].toInt()
    GatewayClient(ManagedChannelBuilder.forAddress(host, port).usePlaintext().enableRetry().build())
}

@OptIn(ExperimentalUuidApi::class)
class GatewayClient(
    private val channel: ManagedChannel
) : Closeable {
    private val stub: GatewayGrpcKt.GatewayCoroutineStub by lazy {
        GatewayGrpcKt.GatewayCoroutineStub(channel)
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun initiateNetwork(
        network: Network,
        tunInterfaceName: NetworkInterfaceName,
        vrfInterfaceName: NetworkInterfaceName,
        bridgeInterfaceName: NetworkInterfaceName
    ): GatewayOuterClass.InitNetworkResponse {
        return stub.initiateNetwork(
            initNetworkRequest {
                networkId = network.id.toString()
                networkName = network.name
                networkAddress = network.networkAddressWithMask.address.value.toInt()
                networkMask = network.networkAddressWithMask.mask.value.toInt()
                containersNetworkAddress = network.containersNetworkAddressWithMask.address.value.toInt()
                containersNetworkMask = network.containersNetworkAddressWithMask.mask.value.toInt()
                clientsNetworkAddress = network.clientsNetworkAddressWithMask.address.value.toInt()
                clientsNetworkMask = network.clientsNetworkAddressWithMask.mask.value.toInt()
                this.tunInterfaceName = tunInterfaceName.value
                this.vrfInterfaceName = vrfInterfaceName.value
                this.bridgeInterfaceName = bridgeInterfaceName.value
                vrfRouteTableId = network.vrfRouteNumber.toInt()
            }
        )
    }

    suspend fun cleanUpNetwork(network: Network): GatewayOuterClass.CleanNetworkResponse {
        requireNotNull(network.vrfInterfaceName)
        requireNotNull(network.bridgeInterfaceName)
        requireNotNull(network.tunInterfaceName)
        return stub.cleanUpNetwork(
            cleanNetworkRequest {
                networkId = network.id.toString()
                vrfInterfaceName = network.vrfInterfaceName.value
                bridgeInterfaceName = network.bridgeInterfaceName.value
                tunInterfaceName = network.tunInterfaceName.value
                vrfRouteTableId = network.vrfRouteNumber.toInt()

            }
        )
    }

    suspend fun initiateContainerNetwork(
        network: Network,
        bridgeInterfaceName: NetworkInterfaceName,
        vEthInterfaceName: NetworkInterfaceName,
        vEthContainerInterfaceName: NetworkInterfaceName,
        nsPath: String,
        iPv4Address: IPv4Address,
    ): GatewayOuterClass.InitContainerNetworkResponse {
        return stub.initiateContainerNetwork(
            initContainerNetworkRequest {
                this.networkId = network.id.toString()
                this.bridgeInterfaceName = bridgeInterfaceName.value
                this.vethInterfaceName = vEthInterfaceName.value
                this.vethContainerInterfaceName = vEthContainerInterfaceName.value
                this.nsPath = nsPath
                this.ipAddress = iPv4Address.value.toInt()
                this.subnetMask = network.containersNetworkAddressWithMask.mask.value.toInt()
                this.clientNetworkAddress = network.clientsNetworkAddressWithMask.address.value.toInt()
                this.clientGatewayAddress =
                    network.clientsNetworkAddressWithMask.availableIPAddress().last().value.toInt()
                this.containerNetworkAddress = network.containersNetworkAddressWithMask.address.value.toInt()
                this.containerGatewayAddress =
                    network.containersNetworkAddressWithMask.availableIPAddress().last().value.toInt()
            }
        )

    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

}