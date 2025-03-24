package io.github.nitkc_proken.freight.backend.repository

import io.github.nitkc_proken.freight.backend.utils.suspendTransaction
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass

interface Repository<T : Model<ID>, ID : Any> {
    suspend fun list(): List<T>
    suspend fun get(id: ID): T?
    suspend fun create(value: T): T
    suspend fun update(value: T): T
    suspend fun delete(id: ID)
}

// 自動実装
abstract class EntityRepository<T : Model<ID>, ID : Any, E : Entity<ID>, EC : EntityClass<ID, E>>(
    private val entityClass: EC,
    private val entityToModel: EntityToModel<E, T>
) : Repository<T, ID> {
    override suspend fun list(): List<T> = suspendTransaction {
        with(entityToModel) {
            entityClass.all().toModels()
        }
    }

    override suspend fun get(id: ID): T? = suspendTransaction {
        with(entityToModel) {
            entityClass.findById(id)?.toModel()
        }
    }

    override suspend fun delete(id: ID) = suspendTransaction {
        entityClass[id].delete()
    }

    abstract override suspend fun create(value: T): T

    abstract override suspend fun update(value: T): T

}