package io.github.nitkc_proken.freight.backend.values

@JvmInline
value class SubnetMaskLength(val value: Int) {
    init {
        require(value in 0..32) { "Subnet mask length must be in range 0..32" }
    }
}
