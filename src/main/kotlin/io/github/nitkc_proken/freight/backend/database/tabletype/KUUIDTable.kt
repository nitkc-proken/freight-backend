package io.github.nitkc_proken.freight.backend.database.tabletype

import io.github.nitkc_proken.freight.backend.database.columntype.kuuid
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import java.util.*
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
abstract class KUUIDTable(name: String = "", columnName: String = "id") : IdTable<Uuid>(name) {
    final override val id = kuuid(columnName) { autoGenerate() }.entityId()
    final override val primaryKey = PrimaryKey(id)
}

@OptIn(ExperimentalUuidApi::class)
abstract class KUUIDEntity(id: EntityID<Uuid>) : Entity<Uuid>(id)


@OptIn(ExperimentalUuidApi::class)
abstract class KUUIDEntityClass<out E : KUUIDEntity>(
    table: IdTable<Uuid>,
    entityType: Class<E>? = null,
    entityCtor: ((EntityID<Uuid>) -> E)? = null
) : EntityClass<Uuid, E>(table, entityType, entityCtor)