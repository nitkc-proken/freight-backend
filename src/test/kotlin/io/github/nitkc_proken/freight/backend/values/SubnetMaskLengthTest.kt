package io.github.nitkc_proken.freight.backend.values

import io.github.nitkc_proken.freight.backend.utils.ValidatableValue
import io.github.nitkc_proken.freight.backend.utils.validatable
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.intRange
import io.kotest.property.checkAll

class SubnetMaskLengthTest : StringSpec({

    "validate" {
        checkAll(
            Arb.int(0..32)
        ) {
            SubnetMaskLength(it).validatable().validateOrNull().shouldNotBeNull()
        }
        checkAll(
            Arb.int().filterNot { it in 0..32 }
        ) {
            SubnetMaskLength(it).validatable().validateOrNull().shouldBeNull()
        }
    }
})
