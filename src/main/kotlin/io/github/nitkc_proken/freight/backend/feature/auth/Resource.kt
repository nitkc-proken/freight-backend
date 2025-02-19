package io.github.nitkc_proken.freight.backend.feature.auth

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
    @Resource("/logout")
    data class Logout(
        val auth: Auth = Auth(),
    )
}
