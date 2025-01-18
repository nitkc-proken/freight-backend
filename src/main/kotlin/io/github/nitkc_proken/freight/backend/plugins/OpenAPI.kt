package io.github.nitkc_proken.freight.backend.plugins

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.AuthScheme
import io.github.smiley4.ktorswaggerui.data.AuthType
import io.github.smiley4.ktorswaggerui.data.kotlinxExampleEncoder
import io.github.smiley4.ktorswaggerui.routing.openApiSpec
import io.github.smiley4.ktorswaggerui.routing.swaggerUI
import io.github.smiley4.schemakenerator.core.annotations.Format
import io.github.smiley4.schemakenerator.core.connectSubTypes
import io.github.smiley4.schemakenerator.core.data.AnnotationData
import io.github.smiley4.schemakenerator.core.data.PrimitiveTypeData
import io.github.smiley4.schemakenerator.core.data.TypeId
import io.github.smiley4.schemakenerator.core.handleNameAnnotation
import io.github.smiley4.schemakenerator.serialization.processKotlinxSerialization
import io.github.smiley4.schemakenerator.swagger.*
import io.github.smiley4.schemakenerator.swagger.data.TitleType
import io.ktor.server.application.*
import io.ktor.server.routing.*
import kotlinx.datetime.Instant
import java.util.UUID

fun Application.configureOpenAPI() {
    install(SwaggerUI) {
        schemas {
            generator = { type ->
                type
                    .processKotlinxSerialization({
                        customProcessor<UUID> {
                            PrimitiveTypeData(
                                // needs a (unique) id for our type, overwriting UUID so taking that
                                id = TypeId.build(UUID::class.qualifiedName!!),
                                // qualified name must be the one of "String".
                                // The generator currently looks at the qualified name to
                                // determine the type of the swagger schema :/
                                qualifiedName = String::class.qualifiedName!!,
                                // simple name can be anything.
                                // Using uuid here since this will become
                                // the "title" in the swagger schema.
                                simpleName = UUID::class.simpleName!!,
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
                                qualifiedName = String::class.qualifiedName!!
                            )
                        }
                    })
                    .connectSubTypes()
                    .handleNameAnnotation()
                    .generateSwaggerSchema()
                    .withTitle(TitleType.SIMPLE)
                    .compileReferencingRoot()
            }
        }
        info {
            title = "Example API"
            version = "latest"
            description = "Example API for testing and demonstration purposes."
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
    }
    routing {
        // Create a route for the openapi-spec file.
        route("api.json") {
            openApiSpec()
        }
        // Create a route for the swagger-ui using the openapi-spec at "/api.json".
        route("swagger") {
            swaggerUI("/api/api.json")
        }
    }
}
