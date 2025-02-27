import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)

    alias(libs.plugins.ksp)

    alias(libs.plugins.protobuf)
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
        optIn.add("kotlin.uuid.ExperimentalUuidApi")
    }
    sourceSets {
        main {
            kotlin {
                srcDir("build/generated/source/proto/main/kotlin")
                srcDir("build/generated/source/proto/main/java")
                srcDir("build/generated/source/proto/main/grpcKt")
            }
        }
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

    implementation(libs.schema.kenerator.core)
    implementation(libs.schema.kenerator.reflection)
    implementation(libs.schema.kenerator.serialization)
    implementation(libs.schema.kenerator.swagger)
    implementation(libs.ktor.swagger.ui)

    implementation(libs.koin.ktor)
    implementation(libs.koin.slf4j)
    implementation(libs.koin.annotations)

    testImplementation(libs.ktor.test)

    ksp(libs.koin.ksp)

    implementation(libs.spring.security.web)
    implementation(libs.bcprov.jdk18on)
    //implementation(libs.spring.security.crypto)

    implementation(libs.dotenv)

    // gRPC
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)
    implementation(libs.protobuf.kotlin)
    runtimeOnly(libs.grpc.netty)

    implementation(libs.docker.java)
    implementation(libs.docker.java.transport.http)

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



protobuf {
    protoc {
        artifact = libs.protoc.get().toString()
    }
    plugins {
        id("grpc") {
            artifact = libs.grpc.protoc.java.get().toString()
        }
        id("grpckt") {
            artifact = libs.grpc.protoc.kotlin.get().toString() + ":jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("kotlin")
            }
        }

        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}