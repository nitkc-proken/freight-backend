plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)

    alias(libs.plugins.ksp)
}

group = "me.naotiki"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")

}

tasks.shadowJar {
    mergeServiceFiles()
}


repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

dependencies {
    implementation(libs.ktx.datetime)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.openapi)
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.exposed.migration)
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)
    implementation(libs.postgresql)
    implementation(libs.ktor.resources)
    implementation(libs.ktor.sessions)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.apache)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback)
    implementation("io.github.smiley4:schema-kenerator-core:1.6.3")
    implementation("io.github.smiley4:schema-kenerator-reflection:1.6.3")
    implementation("io.github.smiley4:schema-kenerator-serialization:1.6.3")
    implementation("io.github.smiley4:schema-kenerator-swagger:1.6.3")
    implementation("io.github.smiley4:ktor-swagger-ui:4.1.5")

    implementation(libs.koin.ktor)
    implementation(libs.koin.slf4j)
    implementation(libs.koin.annotations)

    testImplementation(libs.ktor.test)

    ksp(libs.koin.ksp)

    implementation(libs.spring.security.web)
    implementation(libs.bcprov.jdk18on)
    //implementation(libs.spring.security.crypto)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.ktor.test)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotest.koin)
    testImplementation(libs.kotest.assertions.ktor)
    testImplementation(libs.koin.test)

}

// Linter
detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    autoCorrect = true
}

tasks.register<JavaExec>("applyMigration") {
    group = "database"
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    mainClass = "io.github.nitkc_proken.freight.database.MigrationKt"
}

tasks.register<JavaExec>("generateMigrationScript") {
    group = "database"
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    mainClass = "io.github.nitkc_proken.freight.database.GenerateMigrationScriptKt"
}

tasks.register<JavaExec>("seed") {
    group = "database"
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    mainClass = "io.github.nitkc_proken.freight.database.SeedKt"
}