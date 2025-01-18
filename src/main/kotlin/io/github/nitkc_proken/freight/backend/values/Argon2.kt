package io.github.nitkc_proken.freight.backend.values

import kotlinx.serialization.Serializable
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

@Serializable
@JvmInline
value class Argon2 private constructor(
    val hashed: String
) {
    companion object {
        private val encoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
        const val LENGTH = 128
        fun fromHashedString(string: String): Argon2 {
            return Argon2(string)
        }

        fun hash(password: String): Argon2 {
            return Argon2(encoder.encode(password))
        }
    }

    infix fun matches(rawPassword: String): Boolean {
        return encoder.matches(rawPassword, hashed)
    }
}
