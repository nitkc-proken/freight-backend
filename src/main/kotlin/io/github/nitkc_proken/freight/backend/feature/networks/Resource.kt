package io.github.nitkc_proken.freight.backend.feature.networks

import io.ktor.resources.Resource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Resource("/networks")
class Networks {
    @Resource("/create")
    class Create(
        val networks: Networks = Networks(),
    )

    @Resource("/me")
    class GetSelfNetworks(
        val networks: Networks = Networks(),
    )

    @OptIn(ExperimentalUuidApi::class)
    @Resource("/id/{id}")
    class ById(
        val id: Uuid,
        val networks: Networks = Networks()
    )

    @Resource("/{owner}/{name}")
    class ByOwnerAndName(
        val owner: String,
        val name: String,
        val networks: Networks = Networks()
    ) {
        @Resource("/init")
        class Init(
            val owner: String,
            val name: String,
            val byOwnerAndName: ByOwnerAndName = ByOwnerAndName(owner, name)
        )

        @Resource("/clean")
        class Clean(
            val owner: String,
            val name: String,
            val byOwnerAndName: ByOwnerAndName = ByOwnerAndName(owner, name)
        )
    }
}
