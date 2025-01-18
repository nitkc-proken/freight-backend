package io.github.nitkc_proken.freight.backend.database.columntype

import io.github.nitkc_proken.freight.backend.values.*
import io.github.nitkc_proken.freight.backend.values.Bcrypt.Companion.BCRYPT_LENGTH
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

fun Table.bcrypt(columnName: String): Column<Bcrypt> = varchar(columnName, BCRYPT_LENGTH).transform({
    Bcrypt.fromHashedString(it)
}, { it.hashed })

fun Table.argon2(columnName: String): Column<Argon2> = varchar(columnName, Argon2.LENGTH).transform({
    Argon2.fromHashedString(it)
}, { it.hashed })

fun Table.ipv4Address(name: String) = integer(name).transform({ IPv4Address(it) }, { it.value })

fun Table.subnetMaskLength(name: String) = integer(name).transform({ SubnetMaskLength(it) }, { it.value })

fun Table.dockerId(name: String) = varchar(name, 64).transform({ DockerId(it) }, { it.value })

fun Table.networkInterfaceName(name: String) = varchar(name, 15).transform({ NetworkInterfaceName(it) }, { it.value })
