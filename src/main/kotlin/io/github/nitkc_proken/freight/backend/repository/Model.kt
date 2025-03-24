package io.github.nitkc_proken.freight.backend.repository

import org.jetbrains.exposed.dao.id.EntityID

interface Model<I : Any> {
    fun toEntityId(): EntityID<I>
}

interface EntityToModel<E, M : Model<*>> {
    fun E.toModel(): M

    fun Iterable<E>.toModels(): List<M> {
        return map { it.toModel() }
    }
}
