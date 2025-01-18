package io.github.nitkc_proken.freight.backend.database.postgres

import io.github.nitkc_proken.freight.backend.values.Permissions
import org.postgresql.util.PGobject
import kotlin.reflect.KClass


class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

typealias EnumKlass = KClass<Permissions>

inline fun EnumKlass.enumKlassToPostgresTypeName(): String {
    return simpleName!!.lowercase()
}

private fun createEnumTypeSchema(enumKlass: EnumKlass): String {
    return "CREATE TYPE ${enumKlass.enumKlassToPostgresTypeName()} AS ENUM ('${
        enumKlass.java.enumConstants.joinToString(
            "', '"
        )
    }')"
}

private fun createEnumSchemas(enumKlass: EnumKlass): String {
    val name = enumKlass.enumKlassToPostgresTypeName()
    return """
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = '$name') THEN
            ${createEnumTypeSchema(enumKlass)};
        END IF;
    """.trimIndent()
}

fun createSafetyEnumTypeSchema(vararg enumKlass: EnumKlass): String {
    return """
        DO ${'$'}${'$'}
        BEGIN
            ${
        enumKlass.map {
            createEnumSchemas(it)
        }.joinToString("\n")
    }
        END${'$'}${'$'};
    """.trimIndent()
}