package io.github.nitkc_proken.freight.utils

import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv {
    filename = if (System.getenv("ENV") == "production") ".env" else ".env.dev"
}
