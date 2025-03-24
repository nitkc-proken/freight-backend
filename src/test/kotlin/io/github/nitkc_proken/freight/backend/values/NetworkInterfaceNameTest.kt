package io.github.nitkc_proken.freight.backend.values

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.uInt
import io.kotest.property.checkAll

class NetworkInterfaceNameTest : StringSpec({

    "generateName" {
        checkAll(
            Arb.uInt()
        ) {
            val name = NetworkInterfaceName.generateNICName(it, "f8_nwt-",)
            println(it to name)
        }
    }
})
