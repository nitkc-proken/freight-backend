package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext

@JvmInline
value class NetworkInterfaceName(val value: String) : Validatable {
    override fun ValidationContext.validate() {
        should(value.length <= 15, "NetworkInterfaceName must be 15 characters or less")
    }
}
