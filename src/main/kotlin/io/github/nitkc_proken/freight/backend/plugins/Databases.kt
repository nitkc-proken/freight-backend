package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.database.DBConfig
import io.github.nitkc_proken.freight.database.migration
import io.github.nitkc_proken.freight.utils.dotenv
import io.ktor.server.application.*

fun Application.configureDatabases() {
    val url = dotenv["JDBC_URL"]
    val user = dotenv["DB_USER"]
    val password = dotenv["DB_PASSWORD"]
    val dbConfig = DBConfig(
        url = url,
        user = user,
        password = password
    )
    dbConfig.connect()
    migration(dbConfig)
}
