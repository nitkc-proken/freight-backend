package io.github.nitkc_proken.freight.backend.values

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@JvmInline
value class Bcrypt private constructor(
    val hashed: String
) {
    companion object {
        private val bCryptEncoder = BCryptPasswordEncoder()
        const val LENGTH = 60
        fun fromHashedString(string: String): Bcrypt {
            return Bcrypt(string)
        }

        fun bcryptHash(password: String): Bcrypt {
            return Bcrypt(bCryptEncoder.encode(password))
        }
    }
}
