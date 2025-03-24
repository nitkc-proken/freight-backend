package io.github.nitkc_proken.freight.backend.database.columntype

import io.github.nitkc_proken.freight.backend.database.tables.UsersTable.clientDefault
import io.github.nitkc_proken.freight.backend.values.*
import io.github.nitkc_proken.freight.backend.values.Bcrypt.Companion.LENGTH
import org.jetbrains.exposed.sql.*
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid
import kotlin.uuid.toKotlinUuid

fun Table.bcrypt(columnName: String): Column<Bcrypt> = varchar(columnName, LENGTH).transform({
    Bcrypt.fromHashedString(it)
}, { it.hashed })

fun Table.argon2(columnName: String): Column<Argon2> = varchar(columnName, Argon2.LENGTH).transform({
    Argon2.fromHashedString(it)
}, { it.hashed })

fun Table.ipv4Address(name: String) = uinteger(name).transform({ IPv4Address(it) }, { it.value })

fun Table.subnetMaskLength(name: String) = ubyte(name).transform({ SubnetMaskLength(it) }, { it.value })

fun Table.networkAddressWithMask(name: String) =
    ulong(name).transform({ NetworkAddressWithMask.fromBits(it) }, { it.toBits() })

fun Table.dockerId(name: String) = varchar(name, 64).transform({ DockerId(it) }, { it.value })

fun Table.networkInterfaceName(name: String) = varchar(name, 15).transform({ NetworkInterfaceName(it) }, { it.value })

@OptIn(ExperimentalUuidApi::class)
fun Table.kuuid(
    name: String,
    preprocess: Column<UUID>.() -> Column<UUID> = {
        this
    }
) = uuid(name).preprocess().transform({ it.toKotlinUuid() }, { it.toJavaUuid() })

fun Column<Uuid>.autoGenerate() = clientDefault {
    Uuid.random()
}

fun Table.nanoid(
    name: String,
    length: Int = 21,
    preprocess: Column<String>.() -> Column<String> = {
        this
    }
) = varchar(name, length).preprocess().transform({ NanoId.fromString(it) }, { it.value })
