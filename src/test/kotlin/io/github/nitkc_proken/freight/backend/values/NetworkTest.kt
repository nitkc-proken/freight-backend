package io.github.nitkc_proken.freight.backend.values

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NetworkTest : StringSpec({
    "subnet" {
        val nw = NetworkAddressWithMask.fromCIDROrNull("192.168.0.0/16")!!
        val subnet = NetworkAddressWithMask.fromCIDROrNull("192.168.1.0/24")!!
        (subnet in nw) shouldBe true
        val nw2 = NetworkAddressWithMask.fromCIDROrNull("172.22.1.0/24")!!
        val subnet2 = NetworkAddressWithMask.fromCIDROrNull("172.22.2.0/24")!!
        (subnet2 in nw2) shouldBe false
    }
    "address range" {
        val nw = NetworkAddressWithMask.fromCIDROrNull("192.168.1.0/24")!!
        val range = nw.availableIPAddress()
        for (i in range) {
            println(i)
        }
    }
})
