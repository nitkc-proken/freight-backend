package io.github.nitkc_proken.freight.backend

import io.github.nitkc_proken.freight.backend.database.DBConfig
import io.github.nitkc_proken.freight.backend.database.additionalSQL
import io.github.nitkc_proken.freight.backend.database.dbConfig
import io.github.nitkc_proken.freight.backend.database.tables
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    migration()
}

fun isNeedToMigration() = transaction(dbConfig.connect()) {
    MigrationUtils.statementsRequiredForDatabaseMigration(*tables, withLogs = false).isNotEmpty()
}

fun migration(baseline: Boolean = false) {
    val flyway = defaultFlywayConfig
        .baselineOnMigrate(baseline) // 既存のデータベースを初めて移行するときに使用します
        .load()
    transaction(dbConfig.connect()) {
        additionalSQL.forEach {
            exec(it)
        }
        flyway.migrate()
    }
}

val defaultFlywayConfig = dbConfig.toFlywayConfig()
    .locations("filesystem:$MIGRATIONS_DIRECTORY")

fun DBConfig.toFlywayConfig() = Flyway.configure()
    .dataSource(url, user, password)
