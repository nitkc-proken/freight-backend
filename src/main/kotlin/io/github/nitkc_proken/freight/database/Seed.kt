package io.github.nitkc_proken.freight.database

import io.github.nitkc_proken.freight.backend.database.getDBConfigFromEnv
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.values.Argon2
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val dbConfig = getDBConfigFromEnv()
    val db = dbConfig.connect()
    if (isNeedToMigration()) {
        println("Need to migrate...")
        migration(dbConfig)
    }
    println("Seeding...")
    seed(db)
}

val seedUsers = arrayOf(
    "test1" to "password",
    "test2" to "password",
    "test3" to "password",
    "test4" to "password",
    "test5" to "password",
    "admin" to "admin",
    "guest" to "guest"

)

fun seed(db: Database) = transaction(db) {
    seedUsers.forEach { (username, password) ->
        UserEntity.new {
            this.username = username
            this.passwordHash = Argon2.hash(password)
        }
    }
}
