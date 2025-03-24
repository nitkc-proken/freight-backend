package io.github.nitkc_proken.freight.backend.repository

interface Repository<T : Model<ID>, ID : Any> {
    suspend fun list(): List<T>
    suspend fun get(id: ID): T?
    suspend fun create(t: T): T
    suspend fun update(t: T): T
    suspend fun delete(id: ID)
}
