package io.github.nitkc_proken.freight.backend.feature.networks.model

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import io.github.nitkc_proken.freight.backend.values.NetworkAddressWithMask
import io.github.smiley4.schemakenerator.core.annotations.Example
import io.github.smiley4.schemakenerator.core.annotations.Format
import io.github.smiley4.schemakenerator.core.annotations.Type
import io.github.smiley4.schemakenerator.swagger.data.SwaggerTypeHint
import kotlinx.serialization.Serializable

@Serializable
data class CreateNetworkRequest(
    val name: String,
    val networkAddress: NetworkAddressWithMask,
    val containerAddress: NetworkAddressWithMask,
    val clientAddress: NetworkAddressWithMask
) : Validatable {
    context(ValidationContext)
    override fun validate() {
        should(containerAddress in networkAddress, "containerAddress should be in networkAddress")
        should(clientAddress in networkAddress, "clientAddress should be in networkAddress")
        should(
            containerAddress isNotOverlappingWith clientAddress,
            "containerAddress and clientAddress should not overlap"
        )
    }
}
