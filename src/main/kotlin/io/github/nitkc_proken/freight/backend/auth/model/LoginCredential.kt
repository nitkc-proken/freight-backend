package io.github.nitkc_proken.freight.backend.auth.model

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import kotlinx.serialization.Serializable

@Serializable
data class LoginCredential(
    val username: String,
    val password: String
) : Validatable {
    context(ValidationContext)
    override fun validate() {
        should(username.isNotBlank())
        should(password.isNotBlank())
    }
}
