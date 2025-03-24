package io.github.nitkc_proken.freight.backend.entity

import io.github.nitkc_proken.freight.backend.database.tables.NetworkMembersTable
import org.jetbrains.exposed.dao.CompositeEntity
import org.jetbrains.exposed.dao.CompositeEntityClass
import org.jetbrains.exposed.dao.id.CompositeID
import org.jetbrains.exposed.dao.id.EntityID

class NetworkMemberEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<NetworkMemberEntity>(NetworkMembersTable)

    var network by NetworkEntity referencedOn NetworkMembersTable.network
    var member by UserEntity referencedOn NetworkMembersTable.user
    var permission by NetworkMembersTable.permission
}
