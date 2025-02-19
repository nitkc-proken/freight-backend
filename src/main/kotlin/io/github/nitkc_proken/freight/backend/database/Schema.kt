package io.github.nitkc_proken.freight.backend.database

import io.github.nitkc_proken.freight.backend.database.postgres.createSafetyEnumTypeSchema
import io.github.nitkc_proken.freight.backend.database.tables.*
import io.github.nitkc_proken.freight.backend.values.Permissions

val tables = arrayOf(
    UsersTable,
    NetworksTable,
    NetworkMembersTable,
    TokensTable,
    TunnelSessionsTable,
    ContainersTable,
)

val additionalSQL = buildList {
    add(createSafetyEnumTypeSchema(Permissions::class))
}
