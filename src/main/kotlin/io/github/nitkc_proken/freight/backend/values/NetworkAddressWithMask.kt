package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import io.github.nitkc_proken.freight.backend.utils.validatable
import io.github.smiley4.schemakenerator.core.annotations.Example
import io.github.smiley4.schemakenerator.core.annotations.Format
import io.github.smiley4.schemakenerator.core.annotations.Type
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Type("string")
@Format("ipv4-cidr")
@Example("10.0.0.0/8")
@Serializable(with = NetworkAddressAsStringSerializer::class)
data class NetworkAddressWithMask(
    val address: IPv4Address,
    val mask: SubnetMaskLength
) : Validatable {
    context(ValidationContext)
    override fun validate() {
        mask.validate()
        address.validate()
        // サブネットマスクの長さが正しいか
        should(address.value and mask.toBitMask() == address.value)
    }

    operator fun contains(subnet: NetworkAddressWithMask): Boolean {
        return subnet.mask.value >= mask.value &&
            subnet.address.value and mask.toBitMask() == address.value and mask.toBitMask()
    }

    // IPの範囲が被っていないかどうか
    infix fun isNotOverlappingWith(subnet: NetworkAddressWithMask): Boolean {
        return subnet.address.value and mask.toBitMask() != address.value and mask.toBitMask()
    }

    operator fun contains(ip: IPv4Address): Boolean {
        return ip.value and mask.toBitMask() == address.value and mask.toBitMask()
    }

    fun toBits(): ULong {
        return (address.value.toULong() shl 8) or mask.value.toULong()
    }

    fun availableIPAddress(): IPv4AddressRange {
        val availableIPs = 2u shl ((32u - mask.value).toInt())
        val broadcastAddress = address.value or (availableIPs - 1u)
        val firstAddress = address.value + 1u
        val lastAddress = broadcastAddress - 1u
        return IPv4AddressRange(IPv4Address(firstAddress), IPv4Address(lastAddress))
    }

    companion object {
        fun fromCIDROrNull(value: String): NetworkAddressWithMask? {
            val parts = value.split("/")
            if (parts.size != 2) return null
            val address = IPv4Address.fromStringOrNull(parts[0]) ?: return null
            val mask =
                SubnetMaskLength(parts[1].toUByteOrNull() ?: return null).validatable().validateOrNull() ?: return null
            return NetworkAddressWithMask(address, mask)
        }

        fun fromBits(value: ULong): NetworkAddressWithMask {
            val address = (value shr 8).toUInt()
            val mask = (value and 0xFFu).toUByte()
            val addressObj = IPv4Address(address)
            val maskObj = SubnetMaskLength(mask)
            return NetworkAddressWithMask(addressObj, maskObj)
        }
    }
}

object NetworkAddressAsStringSerializer : KSerializer<NetworkAddressWithMask> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "io.github.nitkc_proken.freight.backend.values.NetworkAddressWithMask",
            PrimitiveKind.STRING
        )

    override fun serialize(encoder: Encoder, value: NetworkAddressWithMask) {
        encoder.encodeString("${value.address}/${value.mask.value}")
    }

    override fun deserialize(decoder: Decoder): NetworkAddressWithMask {
        return NetworkAddressWithMask.fromCIDROrNull(decoder.decodeString())
            ?: throw SerializationException("Invalid CIDR format")
    }
}
