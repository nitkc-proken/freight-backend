package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import io.github.nitkc_proken.freight.backend.utils.validatable
import kotlinx.serialization.Serializable

@Serializable
data class Network(
    val address: IPv4Address,
    val mask: SubnetMaskLength
) : Validatable {
    context(ValidationContext)
    override fun validate() {
        mask.validate()
        address.validate()
        //サブネットマスクの長さが正しいか
        should(address.value and mask.toBitMask() == address.value)
    }

    companion object {
        fun fromStringOrNull(value: String): Network? {
            val parts = value.split("/")
            if (parts.size != 2) return null
            val address = IPv4Address.fromStringOrNull(parts[0]) ?: return null
            val mask =
                SubnetMaskLength(parts[1].toIntOrNull() ?: return null).validatable().validateOrNull() ?: return null
            return Network(address, mask)
        }
    }
}