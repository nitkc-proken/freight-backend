package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.ValidatableValue
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.ipAddressV4
import io.kotest.property.arbitrary.ipAddressV6
import io.kotest.property.checkAll

class IPv4AddressTest : StringSpec({
    "IPv4 Parse" {
        checkAll(
            Arb.ipAddressV4()
        ) {
            val addr = IPv4Address.fromStringOrNull(it).shouldNotBeNull()
            addr.toString() shouldBe it
        }
    }
    "IPv4 Parse Fail" {
        IPv4Address.fromStringOrNull("256.0.0.0") shouldBe null
        IPv4Address.fromStringOrNull("-1.0.0.0") shouldBe null
        IPv4Address.fromStringOrNull("hello! world") shouldBe null
        IPv4Address.fromStringOrNull("a.b.c.d") shouldBe null
        checkAll(Arb.ipAddressV6()) {
            IPv4Address.fromStringOrNull(it) shouldBe null
        }
    }
    "validate" {
        checkAll(
            Arb.ipAddressV4()
        ) {
            shouldNotThrow<IllegalArgumentException> {
                ValidatableValue(IPv4Address.fromStringOrNull(it).shouldNotBeNull()).validate()
            }
        }
    }
})