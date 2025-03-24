package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class IPv4Address(
    val value: UInt
) : Validatable, Comparable<IPv4Address> {
    context(ValidationContext)
    override fun validate() {
        should(value in IPV4_ADDRESS_RANGE)
    }

    override fun compareTo(other: IPv4Address): Int = value.compareTo(other.value)

    override fun toString(): String {
        return "${value shr 24 and 255u}.${value shr 16 and 255u}.${value shr 8 and 255u}.${value and 255u}"
    }

    operator fun plus(increment: UInt): IPv4Address {
        return IPv4Address(value + increment)
    }

    operator fun minus(decrement: UInt): IPv4Address {
        return IPv4Address(value - decrement)
    }

    operator fun rangeTo(other: IPv4Address): IPv4AddressRange {
        return IPv4AddressRange(this, other)
    }

    operator fun rangeUntil(other: IPv4Address): IPv4AddressRange {
        return IPv4AddressRange(this, other - 1u)
    }

    companion object {
        val IPV4_ADDRESS_RANGE = 0u..0xFFFFFFFFu
        fun fromStringOrNull(value: String): IPv4Address? {
            val number = value.split(".").map {
                it.toUIntOrNull() ?: return null
            }
            if (number.size != 4) return null
            if (!number.all { it in 0u..255u }) return null
            return IPv4Address(
                (number[0] shl 24) or (number[1] shl 16) or (number[2] shl 8) or number[3]
            )
        }
    }
}

class IPv4AddressRange(
    val start: IPv4Address,
    val endInclusive: IPv4Address
) : Iterable<IPv4Address> {
    override fun iterator(): Iterator<IPv4Address> {
        return object : Iterator<IPv4Address> {
            private var current = start

            override fun hasNext(): Boolean {
                return current <= endInclusive
            }

            override fun next(): IPv4Address {
                if (!hasNext()) throw NoSuchElementException()
                val result = current
                current += 1u
                return result
            }
        }
    }
}
