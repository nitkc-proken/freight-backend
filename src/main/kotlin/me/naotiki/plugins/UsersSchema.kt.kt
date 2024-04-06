package me.naotiki.plugins

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.*

@Serializable
data class ExposedUser(val name: String, val age: Int)
class UserService(private val database: Database) {
    object Users : Table("teams") {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age")
        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
        if (true) {
        } else {

        }

    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: ExposedUser): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[age] = user.age
        }[Users.id]
    }

    suspend fun read(id: Int): ExposedUser? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map { ExposedUser(it[Users.name], it[Users.age]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, user: ExposedUser) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}

object DUsers : UUIDTable() {
    val displayId = varchar("display_id", 50).uniqueIndex()
    val keycloakId = varchar("keycloak_id", 50).uniqueIndex()
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}

class DUser(id: EntityID<UUID>) : UUIDEntity(id) {
    init {
        EntityHook.subscribe {
        }
    }

    companion object : UUIDEntityClass<DUser>(DUsers)

    var displayId by DUsers.displayId
    var keycloakId by DUsers.keycloakId
    var createdAt by DUsers.createdAt
    var updatedAt by DUsers.updatedAt.apply {
    }
}

abstract class ExposedTimestampIdTable(name: String = "", columnName: String = "id") : IntIdTable(name, columnName) {
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }
}

abstract class ExposedTimestampIdEntity(id: EntityID<Int>, table: ExposedTimestampIdTable) : IntEntity(id) {
    val createdAt by table.createdAt
    var updatedAt by table.updatedAt
}

abstract class ExposedTimestampIdEntityClass<E : ExposedTimestampIdEntity>(table: ExposedTimestampIdTable) :
    IntEntityClass<E>(table) {
    init {
        EntityHook.subscribe { action ->
            if (action.changeType == EntityChangeType.Updated) {
                action.toEntity(this)?.updatedAt = Instant.now()
            }
        }
    }
}
