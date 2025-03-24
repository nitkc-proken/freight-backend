package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.validatable
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.uByte
import io.kotest.property.checkAll

class SubnetMaskLengthTest : StringSpec({

    "validate" {
        checkAll(
            Arb.uByte(0u, 32u)
        ) {
            SubnetMaskLength(it).validatable().validateOrNull().shouldNotBeNull()
        }
        checkAll(
            Arb.uByte().filterNot { it in 0u..32u }
        ) {
            SubnetMaskLength(it).validatable().validateOrNull().shouldBeNull()
        }
    }
})
