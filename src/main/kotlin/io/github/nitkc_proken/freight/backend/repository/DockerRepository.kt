package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.values.DockerId

interface DockerRepository {
    fun createContainer(image: String): DockerId
    fun startContainer(containerId: DockerId): Boolean
    fun inspectContainer(containerId: DockerId): DockerContainerDetail
    fun removeContainer(containerId: DockerId, force: Boolean)
}

data class DockerContainer(
    val id: DockerId,
    val name: String,
    val image: String,
    val status: String,
)

data class DockerContainerDetail(
    val networkNamespacePath: String,
)
