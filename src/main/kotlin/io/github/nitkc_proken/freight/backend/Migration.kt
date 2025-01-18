package io.github.nitkc_proken.freight.backend

import MigrationUtils
import io.github.nitkc_proken.freight.backend.database.DBConfig
import io.github.nitkc_proken.freight.backend.database.additionalSQL
import io.github.nitkc_proken.freight.backend.database.getDBConfigFromEnv
import io.github.nitkc_proken.freight.backend.database.tables
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.FluentConfiguration
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    val db = getDBConfigFromEnv()
    db.connect()
    migration(db)
}

fun isNeedToMigration() = transaction() {
    MigrationUtils.statementsRequiredForDatabaseMigration(*tables, withLogs = false).isNotEmpty()
}

fun migration(dbConfig: DBConfig, baseline: Boolean = false) {
    val flyway = dbConfig.toFlywayConfig()
        .locations("filesystem:$MIGRATIONS_DIRECTORY")
        .baselineOnMigrate(baseline) // 既存のデータベースを初めて移行するときに使用します
        .load()
    transaction() {
        additionalSQL.forEach {
            exec(it)
        }
        flyway.migrate()
    }
}


fun DBConfig.toFlywayConfig(): FluentConfiguration = Flyway.configure()
    .dataSource(url, user, password)
