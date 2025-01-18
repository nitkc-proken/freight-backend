@file:OptIn(ExperimentalDatabaseMigrationApi::class)

package io.github.nitkc_proken.freight.backend

import MigrationUtils
import io.github.nitkc_proken.freight.backend.database.getDBConfigFromEnv
import io.github.nitkc_proken.freight.backend.database.tables
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

const val MIGRATIONS_DIRECTORY = "migrations" // Location of migration scripts

@Serializable
data class MigrationScriptName(
    val version: Int,
    val name: String
) {
    override fun toString(): String =
        "V${version}__$name"
}

@Serializable
data class MigrationMetadata(
    val latest: MigrationScriptName,
)

@OptIn(ExperimentalSerializationApi::class)
fun main(vararg args: String) = transaction(getDBConfigFromEnv().connect()) {

    if (!isNeedToMigration()) {
        println("No migration required")
        return@transaction
    }
    var migrationName: String?
    do {
        print("what is new migration name? :")
        migrationName = readln()
    } while (migrationName.isNullOrBlank())

    val metadataFile = File("$MIGRATIONS_DIRECTORY/migration-metadata.json")

    val metadata = if (metadataFile.exists()) {
        Json.decodeFromStream<MigrationMetadata>(metadataFile.inputStream())
    } else {
        null
    }

    val nextVersion = metadata?.latest?.version?.plus(1) ?: 1

    val newMigration = MigrationScriptName(nextVersion, migrationName)

    generateMigrationScript(
        MigrationScriptName(nextVersion, migrationName).toString(),
        *tables,
    )

    metadataFile.writeText(
        Json.encodeToString(
            metadata?.copy(latest = newMigration) ?: MigrationMetadata(newMigration)
        )
    )
}

fun generateMigrationScript(name: String, vararg tables: Table) {
    // This will generate a migration script in the path exposed-migration/migrations
    MigrationUtils.generateMigrationScript(
        *tables,
        scriptDirectory = MIGRATIONS_DIRECTORY,
        scriptName = name
    )
}
