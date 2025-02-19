package io.github.nitkc_proken.freight.backend.grpc


import backend.*
import io.github.nitkc_proken.freight.backend.database.tables.NetworksTable
import io.github.nitkc_proken.freight.backend.database.tables.TunnelSessionsTable.clientIp
import io.github.nitkc_proken.freight.backend.database.tables.UsersTable
import io.github.nitkc_proken.freight.backend.feature.networks.NetworkService
import io.github.nitkc_proken.freight.backend.repository.NetworkRepository
import io.github.nitkc_proken.freight.backend.repository.TokenRepository
import io.github.nitkc_proken.freight.backend.repository.TunnelSessionRepository
import io.grpc.Server
import io.grpc.ServerBuilder
import org.jetbrains.exposed.dao.id.EntityID
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*
import kotlin.uuid.ExperimentalUuidApi

class BackendServer(
    private val port: Int = 4000,
    private val server: Server = ServerBuilder.forPort(port).addService(BackendService()).build()
) {
    fun start() {
        server.start()
        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("*** shutting down gRPC server since JVM is shutting down")
                this@BackendServer.stop()
                println("*** server shut down")
            }
        )
    }

    fun stop() {
        server.shutdown()
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }

    internal class BackendService : BackendGrpcKt.BackendCoroutineImplBase(), KoinComponent {
        private val tokenRepository by inject<TokenRepository>()
        private val networkRepository by inject<NetworkRepository>()
        private val tunnelSessionRepository by inject<TunnelSessionRepository>()

        @OptIn(ExperimentalUuidApi::class)
        override suspend fun validateAuthenticationToken(
            request: BackendOuterClass.AuthenticationToken
        ): BackendOuterClass.AuthResponse {
            val user = tokenRepository.getUserFromValidToken(request.token) ?: throw Exception("Invalid token")
            return authResponse {
                userId = user.id.toString()
            }
        }

        @OptIn(ExperimentalUuidApi::class)
        override suspend fun startTunnelSession(
            request: BackendOuterClass.StartTunnelingSessionRequest
        ): BackendOuterClass.StartTunnelingSessionResponse {
            //TODO Token Check
            val network = networkRepository.getJoinedNetworkById(
                EntityID(UUID.fromString(request.userId), UsersTable),
                EntityID(UUID.fromString(request.networkId), NetworksTable)
            )
            if (network == null) {
                throw Exception("GRPC:Network not found")
            }
            val used = networkRepository.listUsedIPAddress(EntityID(UUID.fromString(request.networkId), NetworksTable))
            val ip = network.clientsNetworkAddressWithMask.availableIPAddress().firstOrNull {
                it !in used
            }
            val session = tunnelSessionRepository.createTunnelSession(
                EntityID(UUID.fromString(request.userId), UsersTable),
                network.toEntityId(),
                ip!!
            )
            return startTunnelingSessionResponse {
                sessionId = session.id.toString()
                this.clientIpAddress = session.ipAddress.value.toInt()
            }
        }

        override suspend fun closeTunnelSession(
            request: BackendOuterClass.CloseTunnelSessionRequest
        ): BackendOuterClass.CloseTunnelSessionResponse {
            return closeTunnelSessionResponse {

            }
        }
    }
}
