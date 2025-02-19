package io.github.nitkc_proken.freight.backend.feature.containers

import io.ktor.resources.*

@Resource("/containers")
class Containers {
    @Resource("/create")
    class Create(
        val containers: Containers = Containers(),
    )

}