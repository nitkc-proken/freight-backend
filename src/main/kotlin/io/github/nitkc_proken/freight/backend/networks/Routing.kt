package io.github.nitkc_proken.freight.backend.networks

import io.github.nitkc_proken.freight.backend.utils.documentedGet
import io.github.nitkc_proken.freight.backend.utils.documentedPost
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        authenticate {
          /*  documentedPost<Networks.Create> {

            }*/
        }
    }
}
