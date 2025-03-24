package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class SubnetMaskLength(val value: UByte) : Validatable {
    context(ValidationContext)
    override fun validate() {
        should(value in 0u..32u, "SubnetMaskLength must be in 0..32 but $value")
    }

    fun toBitMask(): UInt {
        return (0xFFFFFFFFu shl ((32u - value).toInt()))
    }
}
