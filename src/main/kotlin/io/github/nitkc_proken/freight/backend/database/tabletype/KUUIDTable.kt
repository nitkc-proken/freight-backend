package io.github.nitkc_proken.freight.backend.database.tabletype

import io.github.nitkc_proken.freight.backend.database.tables.TokensTable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
abstract class KUUIDTable(name: String = "") : IdTable<String>(name) {
    @OptIn(ExperimentalUuidApi::class)
    override val id = varchar("id", 128)
        .clientDefault {
            Uuid.random().toString()
        }.entityId()
}

