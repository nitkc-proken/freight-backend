package io.github.nitkc_proken.freight.backend.utils

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ValidatableValue<T : Validatable>(private val value: T) {
    private fun validateWithContext(): ValidationContext {
        val ctx = ValidationContext()
        with(
            ctx
        ) {
            value.validate()
        }
        return ctx
    }

    fun validate(): T {
        return validateWithResult().getOrThrow()
    }

    fun validateOrNull(): T? {
        return validateWithResult().getOrNull()
    }

    fun validateWithResult(): Result<T> {
        val context = validateWithContext()
        val error = context.throwable
        return if (error == null) {
            Result.success(value)
        } else {
            Result.failure(error)
        }
    }
}

class ValidationContext {
    private val errors = mutableListOf<String?>()
    val isValid: Boolean
        get() = errors.isEmpty()
    val errorMessages: List<String>
        get() = errors.filterNotNull()
    val throwable
        get() = if (errorMessages.isEmpty()) {
            null
        } else {
            IllegalArgumentException(errorMessages.joinToString())
        }

    fun should(condition: Boolean, message: String? = null) {
        if (!condition) {
            errors.add(message)
        }
    }

    fun shouldNot(condition: Boolean, message: String? = null) {
        should(!condition, message)
    }

    fun String.canSplitBy(
        delimiter: String,
        size: Int,
        condition: (String.(index: Int) -> Boolean)? = null
    ): Boolean = this.split(delimiter).let { list ->
        list.size == size &&
            (
                condition == null || list.mapIndexed { index, element -> condition(element, index) }
                    .all { it }
                )
    }
}

interface Validatable {
    context(ValidationContext)
    fun validate()
}

fun <T : Validatable> T.validatable(): ValidatableValue<T> {
    return ValidatableValue(this)
}
