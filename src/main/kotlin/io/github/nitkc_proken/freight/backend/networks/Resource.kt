package io.github.nitkc_proken.freight.backend.networks

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
    @Resource("/{id}")
    class ById(
        val id: Uuid,
        val networks: Networks = Networks()
    )


}
