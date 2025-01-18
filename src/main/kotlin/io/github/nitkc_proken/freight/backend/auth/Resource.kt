package io.github.nitkc_proken.freight.backend.auth

import io.github.nitkc_proken.freight.backend.auth.model.LoginCredential
import io.github.nitkc_proken.freight.backend.utils.ValidatableValue
import io.ktor.resources.*

@Resource("/auth")
class Auth {
    @Resource("/login")
    data class Login(
        val auth: Auth = Auth(),
    )

    @Resource("/register")
    data class Register(
        val auth: Auth = Auth(),
    )
}