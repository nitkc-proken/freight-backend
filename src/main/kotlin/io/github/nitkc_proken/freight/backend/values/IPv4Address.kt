package io.github.nitkc_proken.freight.backend.values

@JvmInline
value class IPv4Address(
    val value: Int
) {
    override fun toString(): String {
        return "${value shr 24 and 0xff}.${value shr 16 and 0xff}.${value shr 8 and 0xff}.${value and 0xff}"
    }

    companion object {
        fun fromStringOrNull(value: String): IPv4Address? {
            val number = value.split(".").map {
                it.toIntOrNull() ?: return null
            }
            if (number.size != 4) return null

            return IPv4Address(
                (number[0] shl 24) or
                    (number[1] shl 16) or
                    (number[2] shl 8) or
                    number[3]
            )
        }
    }
}
