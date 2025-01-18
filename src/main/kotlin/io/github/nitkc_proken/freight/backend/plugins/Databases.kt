package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.database.dbConfig
import io.github.nitkc_proken.freight.backend.migration
import io.ktor.server.application.*

fun Application.configureDatabases() {
    migration()
    dbConfig.connect()
}
