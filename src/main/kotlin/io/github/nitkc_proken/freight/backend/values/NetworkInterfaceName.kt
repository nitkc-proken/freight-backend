package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class NetworkInterfaceName(val value: String) : Validatable {
    context(ValidationContext)
    override fun validate() {
        should(value.length <= 15, "NetworkInterfaceName must be 15 characters or less")
        shouldNot(value.contains(Regex("[/\\s]")), "NetworkInterfaceName must not contain '/' or whitespace")
    }

    companion object {
        val NIC_NAME_CHARACTERS = (('a'..'z') + ('A'..'Z') + ('0'..'9')).toCharArray() +
                "!\"#\$%&'()*+,-.:;<=>?@[]^_`{|}~".toCharArray()

        @Deprecated("Use StringEncoder")
        // UInt id部は最大6文字
        fun generateNICName(id: UInt, prefix: String = "f", suffix: String = ""): NetworkInterfaceName {
            val base = NIC_NAME_CHARACTERS.size.toUInt()

            val digits = StringBuilder()
            // 0の場合は、集合の先頭（例：'a'）を使う
            if (id == 0u) digits.append(NIC_NAME_CHARACTERS.first())

            var n = id
            while (n > 0u) {
                val remainder = n % base
                // 余りに対応する文字を追加
                digits.append(NIC_NAME_CHARACTERS[remainder.toInt()])
                n /= base
            }
            val idConverted = digits.reverse().toString()
            val name = prefix + idConverted + suffix
            require(name.length <= 15) { "NetworkInterfaceName must be 15 characters or less" }
            return NetworkInterfaceName(name)
        }
    }
}
