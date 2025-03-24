package io.github.nitkc_proken.freight.backend.repository.impl

import com.github.dockerjava.api.model.HostConfig
import io.github.nitkc_proken.freight.backend.repository.DockerContainerDetail
import io.github.nitkc_proken.freight.backend.repository.DockerRepository
import io.github.nitkc_proken.freight.backend.utils.dockerClient
import io.github.nitkc_proken.freight.backend.values.DockerId
import org.koin.core.annotation.Single

@Single
class DockerRepositoryImpl : DockerRepository {
    override fun createContainer(image: String): DockerId {
        return dockerClient.createContainerCmd(image)
            .withHostConfig(DefaultHostConfig)
            .withTty(true)
            .withAttachStdin(true)
            .exec().id.let { DockerId(it) }
    }

    override fun startContainer(containerId: DockerId): Boolean {
        return kotlin.runCatching {
            dockerClient.startContainerCmd(containerId.value).exec()
        }.isSuccess
    }

    override fun inspectContainer(containerId: DockerId): DockerContainerDetail {
        return dockerClient.inspectContainerCmd(containerId.value).exec().run {
            DockerContainerDetail(
                networkNamespacePath = networkSettings.sandboxKey
            )
        }
    }

    override fun removeContainer(containerId: DockerId, force: Boolean) {
        dockerClient.removeContainerCmd(containerId.value)
            .withForce(force)
            .exec()
    }

    companion object {
        val DefaultHostConfig = HostConfig.newHostConfig()
            .withNetworkMode("none")
    }
}
