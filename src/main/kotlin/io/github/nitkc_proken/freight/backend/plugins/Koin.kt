package io.github.nitkc_proken.freight.backend.plugins

import io.ktor.server.application.*
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

// Declare a module and scan for annotations
@Module
@ComponentScan("io.github.nitkc_proken.freight.backend")
class KoinModule

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(
            KoinModule().module
        )
    }
}
