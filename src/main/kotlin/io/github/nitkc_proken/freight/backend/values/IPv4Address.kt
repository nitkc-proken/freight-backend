package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class IPv4Address(
    val value: UInt
) : Validatable {
    context(ValidationContext)
    override fun validate() {
        should(value in 0u..0xFFFFFFFFu)
    }

    override fun toString(): String {
        return "${value shr 24 and 255u}.${value shr 16 and 255u}.${value shr 8 and 255u}.${value and 255u}"
    }

    companion object {
        fun fromStringOrNull(value: String): IPv4Address? {
            val number = value.split(".").map {
                it.toIntOrNull() ?: return null
            }
            if (number.size != 4) return null
            if (!number.all { it in 0..255 }) return null

            return IPv4Address(
                (number[0].toUInt() shl 24) or
                        (number[1].toUInt() shl 16) or
                        (number[2].toUInt() shl 8) or
                        number[3].toUInt()
            )
        }
    }
}
