@file:OptIn(ExperimentalDatabaseMigrationApi::class)

package io.github.nitkc_proken.freight.database

import MigrationUtils.statementsRequiredForDatabaseMigration
import io.github.nitkc_proken.freight.backend.database.additionalSQL
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
    val additionalSQLHashed: Int
)

fun List<String>.joinHash(): Int {
    val str = this.joinToString()
    return str.hashCode()

}

@OptIn(ExperimentalSerializationApi::class)
fun main(vararg args: String) = transaction(getDBConfigFromEnv().connect()) {
    val metadataFile = File("$MIGRATIONS_DIRECTORY/migration-metadata.json")

    val metadata = if (metadataFile.exists()) {
        Json.decodeFromStream<MigrationMetadata>(metadataFile.inputStream())
    } else {
        null
    }
    val isOldAdditionalSQL = metadata?.additionalSQLHashed != additionalSQL.joinHash()
    val shouldMigration = isNeedToMigration() || isOldAdditionalSQL
    if (!shouldMigration) {
        println("No migration required")
        return@transaction
    }
    var migrationName: String?
    do {
        print("what is new migration name? :")
        migrationName = readln()
    } while (migrationName.isNullOrBlank())


    val nextVersion = metadata?.latest?.version?.plus(1) ?: 1

    val newMigration = MigrationScriptName(nextVersion, migrationName)

    val file = generateMigrationScript(
        MigrationScriptName(nextVersion, migrationName).toString(),
        additionalSQL.takeIf { isOldAdditionalSQL }?.joinToString("\n"),
        *tables,
    )

    metadataFile.writeText(
        Json.encodeToString(
            metadata?.copy(latest = newMigration) ?: MigrationMetadata(newMigration, additionalSQL.joinHash())
        )
    )
}

fun generateMigrationScript(name: String, preSQL: String? = null, vararg tables: Table): File {
    val allStatements = statementsRequiredForDatabaseMigration(*tables)

    val migrationScript = File("$MIGRATIONS_DIRECTORY/$name.sql")
    migrationScript.createNewFile()

    // Clear existing content
    migrationScript.writeText(if (preSQL != null) "$preSQL\n" else "")
    // Append statements
    allStatements.forEach { statement ->
        // Add semicolon only if it's not already there
        val conditionalSemicolon = if (statement.last() == ';') "" else ";"

        migrationScript.appendText("$statement$conditionalSemicolon\n")
    }

    return migrationScript
}
