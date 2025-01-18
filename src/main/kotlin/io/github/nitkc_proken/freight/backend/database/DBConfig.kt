package io.github.nitkc_proken.freight.backend.database

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import org.jetbrains.exposed.sql.Database

data class DBConfig(
    val url: String,
    val user: String,
    val password: String,
) : Validatable {
    fun connect(): Database {
        return Database.connect(
            url,
            user = user,
            password = password,
        )
    }

    override fun ValidationContext.validate() {
        should(url.isNotBlank())
        should(user.isNotBlank())
        should(password.isNotBlank())
    }
}

fun getDBConfigFromEnv(): DBConfig {
    return DBConfig(
        url = System.getenv("JDBC_URL") ?: "jdbc:postgresql://localhost:15432/freight",
        user = System.getenv("DB_USER") ?: "freight",
        password = System.getenv("DB_PASSWORD") ?: "freight",
    )
}


