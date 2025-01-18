package io.github.nitkc_proken.freight.backend.values

@JvmInline
value class DockerId(val value: String) {
    init {
        require(value.length == 64) { "DockerId must be 64 characters long" }
    }
}
