package io.github.nitkc_proken.freight.backend.database.columntype

import io.github.nitkc_proken.freight.backend.database.postgres.PGEnum
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

inline fun <reified T : Enum<T>> Table.enum(name: String): Column<T> =
    customEnumeration(name, T::class.simpleName, {
        enumValueOf<T>(
            when (it) {
                is PGobject -> it.value
                else -> it
            } as String
        )
    }, { PGEnum(T::class.simpleName.toString(), it) })
