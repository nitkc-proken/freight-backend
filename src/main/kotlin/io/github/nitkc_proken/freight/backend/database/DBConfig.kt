package io.github.nitkc_proken.freight.backend.database

import io.github.nitkc_proken.freight.backend.utils.Validatable
import io.github.nitkc_proken.freight.backend.utils.ValidationContext
import io.github.nitkc_proken.freight.utils.dotenv
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
    context(ValidationContext)
    override fun validate() {
        should(url.isNotBlank())
        should(user.isNotBlank())
        should(password.isNotBlank())
    }
}

fun getDBConfigFromEnv(): DBConfig {
    return DBConfig(
        url = dotenv["JDBC_URL"],
        user = dotenv["DB_USER"],// System.getenv("DB_USER") ?: "freight",
        password = dotenv["DB_PASSWORD"] //System.getenv("DB_PASSWORD") ?: "freight",
    )
}


