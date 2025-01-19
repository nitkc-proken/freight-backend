package io.github.nitkc_proken.freight.backend.networks.model

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import io.github.nitkc_proken.freight.backend.values.IPv4Address
import io.github.nitkc_proken.freight.backend.values.SubnetMaskLength
import kotlinx.serialization.Serializable

@Serializable
data class CreateNetworkRequest(
    val name: String,
    val networkAddress: IPv4Address,
    val subnetMask: SubnetMaskLength,
    val externalAddress: IPv4Address

) : Validatable {
    context(ValidationContext)
    override fun validate() {

    }
}
