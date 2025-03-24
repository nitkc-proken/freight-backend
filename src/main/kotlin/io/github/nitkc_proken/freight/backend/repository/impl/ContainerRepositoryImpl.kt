package io.github.nitkc_proken.freight.backend.repository.impl

import io.github.nitkc_proken.freight.backend.entity.ContainerEntity
import io.github.nitkc_proken.freight.backend.entity.NetworkEntity
import io.github.nitkc_proken.freight.backend.repository.Container
import io.github.nitkc_proken.freight.backend.repository.Container.Companion.toModel
import io.github.nitkc_proken.freight.backend.repository.ContainerRepository
import io.github.nitkc_proken.freight.backend.repository.EntityRepository
import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import org.koin.core.annotation.Single
import kotlin.uuid.Uuid

@Single
class ContainerRepositoryImpl : ContainerRepository,
    EntityRepository<Container, Uuid, ContainerEntity, ContainerEntity.Companion>
        (ContainerEntity, Container) {

    override suspend fun create(value: Container): Container = suspendTransaction {
        ContainerEntity.new(value.id) {
            containerId = value.containerId
            network = NetworkEntity[value.network.toEntityId()]
            ipAddress = value.ipAddress
            hostVEthName = value.hostVEthName
            containerVEthName = value.containerVEthName
            shortId = value.shortId
        }.toModel()
    }

    override suspend fun update(value: Container): Container = suspendTransaction {
        ContainerEntity[value.id].apply {
            containerId = value.containerId
            network = NetworkEntity[value.network.toEntityId()]
            ipAddress = value.ipAddress
            hostVEthName = value.hostVEthName
            containerVEthName = value.containerVEthName
            shortId = value.shortId
        }.toModel()
    }

}
