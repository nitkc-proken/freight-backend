@file:OptIn(ExperimentalUuidApi::class)

package io.github.nitkc_proken.freight.backend.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthScheme
import io.github.smiley4.ktorswaggerui.data.AuthType
import io.github.smiley4.ktorswaggerui.data.OutputFormat
import io.github.smiley4.ktorswaggerui.data.kotlinxExampleEncoder
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.github.smiley4.schemakenerator.core.addDiscriminatorProperty
import io.github.smiley4.schemakenerator.core.annotations.Format
import io.github.smiley4.schemakenerator.core.connectSubTypes
import io.github.smiley4.schemakenerator.core.data.AnnotationData
import io.github.smiley4.schemakenerator.core.data.PrimitiveTypeData
import io.github.smiley4.schemakenerator.core.data.TypeId
import io.github.smiley4.schemakenerator.core.handleNameAnnotation
import io.github.smiley4.schemakenerator.serialization.addJsonClassDiscriminatorProperty
import io.github.smiley4.schemakenerator.serialization.processKotlinxSerialization
import io.github.smiley4.schemakenerator.swagger.*
import io.github.smiley4.schemakenerator.swagger.data.TitleType
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.swagger.v3.core.util.Yaml31
import kotlinx.datetime.Instant
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun Application.configureOpenAPI() {
    install(SwaggerUI) {
        schemas {
            generator = { type ->
                type

                    .processKotlinxSerialization({


                        customProcessor<Uuid> {
                            PrimitiveTypeData(
                                // needs a (unique) id for our type, overwriting UUID so taking that
                                id = TypeId.build(Uuid::class.qualifiedName!!),
                                // qualified name must be the one of "String".
                                // The generator currently looks at the qualified name to
                                // determine the type of the swagger schema :/
                                qualifiedName = String::class.qualifiedName!!,
                                // simple name can be anything.
                                // Using uuid here since this will become
                                // the "title" in the swagger schema.
                                simpleName = Uuid::class.simpleName!!,
                                annotations = mutableListOf(
                                    AnnotationData(
                                        // adding the @Format annotation to our custom type here.
                                        // This way it does not need to be added on every field in the models.
                                        name = Format::class.qualifiedName!!,
                                        values = mutableMapOf("format" to "uuid") // with value "uuid"
                                    )
                                )
                            )
                        }
                        customProcessor<Instant> {
                            PrimitiveTypeData(
                                id = TypeId(
                                    String::class.qualifiedName!!,
                                    emptyList(),
                                    "instant"
                                ),
                                simpleName = String::class.simpleName!!,
                                qualifiedName = String::class.qualifiedName!!,
                                annotations = mutableListOf(
                                    AnnotationData(
                                        // adding the @Format annotation to our custom type here.
                                        // This way it does not need to be added on every field in the models.
                                        name = Format::class.qualifiedName!!,
                                        values = mutableMapOf("format" to "timestamp") // with value "uuid"

                                    )
                                )
                            )
                        }
                    })
                    .addJsonClassDiscriminatorProperty()
                    .addDiscriminatorProperty()
                    .connectSubTypes()
                    .handleNameAnnotation()
                    .generateSwaggerSchema()
                    .handleSchemaAnnotations()
                    .handleCoreAnnotations()
                    .withTitle(TitleType.SIMPLE)
                    .compileReferencing()
            }
        }
        info {
            title = "Freight API"
            version = "latest"
            description = "Freight API"
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }
        examples {
            // configure the example encoder to encode kotlin objects using kotlinx-serializer
            exampleEncoder = kotlinxExampleEncoder
        }
        security {
            val securitySchemeName = "AppDefaultSecurityScheme"
            securityScheme(securitySchemeName) {
                type = AuthType.HTTP
                scheme = AuthScheme.BEARER
            }
            defaultSecuritySchemeNames(securitySchemeName)
        }
        postBuild = { openAPI, id ->
            val file = File("schema.yaml")

            file.writeText(
                Yaml31.pretty(openAPI)
            )
        }
        outputFormat = OutputFormat.YAML
    }
    routing {
        // Create a route for the openapi-spec file.
        route("schema.yaml") {
            openApiSpec()
        }
        // Create a route for the swagger-ui using the openapi-spec at "/api.json".
        route("swagger") {
            swaggerUI("/api/schema.yaml")
        }
    }
}
