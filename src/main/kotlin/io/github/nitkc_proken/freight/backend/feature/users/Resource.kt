package io.github.nitkc_proken.freight.backend.feature.users

import io.ktor.resources.Resource

@Resource("/users")
class Users {
    @Resource("/me")
    data class Me(
        val users: Users = Users(),
    )
}
