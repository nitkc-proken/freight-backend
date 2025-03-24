package io.github.nitkc_proken.freight.backend.values

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import kotlinx.serialization.Serializable
import java.security.SecureRandom
import java.util.Random

@Serializable
@JvmInline
value class NanoId private constructor(val value: String) {
    companion object {
        fun random(
            random: Random,
            alphabet: CharArray,
            size: Int,
        ): NanoId = NanoId(
            NanoIdUtils.randomNanoId(random, alphabet, size)
        )

        fun fromString(value: String): NanoId {
            return NanoId(value)
        }
    }
}

class NanoIdGenerator(
    private val random: Random = DefaultRandom,
    private val alphabet: CharArray = DefaultAlphabet,
    private val size: Int = DEFAULT_SIZE
) {
    companion object {
        val DefaultRandom = SecureRandom()

        val DefaultAlphabet: CharArray =
            "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

        const val DEFAULT_SIZE: Int = 21

        val Default = NanoIdGenerator()
    }

    fun random(): NanoId = NanoId.random(
        random,
        alphabet,
        size
    )
}
