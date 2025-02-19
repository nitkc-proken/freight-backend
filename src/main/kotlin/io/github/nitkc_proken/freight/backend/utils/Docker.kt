package io.github.nitkc_proken.freight.backend.utils

import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import io.github.nitkc_proken.freight.utils.dotenv
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val dockerConfig: DockerClientConfig by lazy {
    DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost(dotenv["DOCKER_HOST"])
        .build()
}

private val dockerHttpClient by lazy {
    ApacheDockerHttpClient.Builder()
        .dockerHost(dockerConfig.dockerHost)
        .sslConfig(dockerConfig.sslConfig)
        .maxConnections(100)
        .connectionTimeout(30.seconds.toJavaDuration())
        .responseTimeout(45.seconds.toJavaDuration())
        .build()
}

val dockerClient by lazy {
    DockerClientImpl.getInstance(dockerConfig, dockerHttpClient)
}