package io.github.nitkc_proken.freight.database

import io.github.nitkc_proken.freight.backend.database.getDBConfigFromEnv
import io.github.nitkc_proken.freight.backend.entity.UserEntity
import io.github.nitkc_proken.freight.backend.values.Argon2
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val db = getDBConfigFromEnv()
    db.connect()
    if (isNeedToMigration()) {
        println("Need to migrate...")
        migration(db)
    }
    println("Seeding...")
    seed()
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

fun seed() = transaction {
    seedUsers.forEach { (username, password) ->
        UserEntity.new {
            this.username = username
            this.passwordHash = Argon2.hash(password)
        }
    }
}