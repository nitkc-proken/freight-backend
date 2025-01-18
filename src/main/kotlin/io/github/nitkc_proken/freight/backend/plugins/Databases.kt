package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.database.DBConfig
import io.github.nitkc_proken.freight.database.migration
import io.ktor.server.application.*

fun Application.configureDatabases() {
    environment.config
    val url = environment.config.property("storage.jdbcURL").getString()
    val user = environment.config.property("storage.user").getString()
    val password = environment.config.property("storage.password").getString()
    val dbConfig = DBConfig(
        url = url,
        user = user,
        password = password
    )
    dbConfig.connect()
    migration(dbConfig)
}
