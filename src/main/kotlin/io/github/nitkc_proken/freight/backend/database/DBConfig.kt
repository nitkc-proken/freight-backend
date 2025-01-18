package io.github.nitkc_proken.freight.backend.database

import org.jetbrains.exposed.sql.Database

data class DBConfig(
    val url: String,
    val user: String,
    val password: String,
) {
    fun connect(): Database {
        return Database.connect(
            url,
            user = user,
            password = password,
        )
    }
}

val dbConfig = DBConfig(
    url = "jdbc:postgresql://localhost:15432/freight",
    user = "freight",
    password = "freight",
)

