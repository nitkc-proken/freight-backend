package io.github.nitkc_proken.freight.backend.feature.containers.model

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import io.github.smiley4.schemakenerator.core.annotations.Example
import kotlinx.serialization.Serializable

@Serializable
data class CreateContainerRequest(
    @Example("naotiki/my-nw1")
    val fullNetworkName: String
) : Validatable {
    context(ValidationContext) override fun validate() {
        should(fullNetworkName.isNotEmpty(), "fullNetworkName should not be empty")

        should(fullNetworkName.canSplitBy("/", 2) { isNotEmpty() }, "fullNetworkName should be splittable by '/'")
    }

    val split get() = fullNetworkName.split("/")
}
