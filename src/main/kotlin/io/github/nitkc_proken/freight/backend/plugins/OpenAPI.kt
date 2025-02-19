@file:OptIn(ExperimentalUuidApi::class)

package io.github.nitkc_proken.freight.backend.plugins

import io.github.nitkc_proken.freight.backend.utils.ResponseResult
import io.github.nitkc_proken.freight.backend.values.NetworkAddressWithMask
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.data.*
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
import io.github.smiley4.schemakenerator.swagger.data.RefType
import io.github.smiley4.schemakenerator.swagger.data.TitleType
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.swagger.v3.core.util.Yaml31
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.typeOf
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun Application.configureOpenAPI() {
    install(SwaggerUI) {
        schemas {
            generator = { type ->
                type

                    .processKotlinxSerialization {
                        customProcessor<NetworkAddressWithMask> {
                            createDefaultPrimitiveTypeData<NetworkAddressWithMask>("string", "ipv4-cidr", "10.0.0.0/8")
                        }
                        customProcessor<Uuid> {
                            createDefaultPrimitiveTypeData<Uuid>("string", "uuid")
                        }
                        customProcessor<Instant> {
                            createDefaultPrimitiveTypeData<Instant>("string", "timestamp")
                        }
                    }
                    .addJsonClassDiscriminatorProperty()
                    .addDiscriminatorProperty()
                    .connectSubTypes()
                    .handleNameAnnotation()
                    .generateSwaggerSchema()
                    .handleCoreAnnotations()
                    .customizeTypes { typeData, typeSchema ->
                        typeData.annotations.find { it.name == "type_format_annotation" }?.also { annotation ->
                            typeSchema.format = annotation.values["format"]?.toString()
                            typeSchema.types = setOf(annotation.values["type"]?.toString())
                            val example = annotation.values["example"]
                            if (example != null) {
                                typeSchema.example = example.toString()
                            }
                        }
                        if (typeData.qualifiedName == ResponseResult.Error::class.qualifiedName) {
                            typeSchema.example =
                                Json.encodeToString(
                                    ResponseResult.Error.serializer(),
                                    ResponseResult.Error("Error Message")
                                )
                        }
                    }
                    .handleSchemaAnnotations()
                    .withTitle(TitleType.SIMPLE)
                    .compileReferencing(RefType.OPENAPI_SIMPLE)

            }
        }
        info {
            title = "Freight API"
            version = "latest"
            description = "Freight API"
            version = "1.0.0"
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

private inline fun <reified T> createDefaultPrimitiveTypeData(
    type: String,
    format: String,
    example: Any? = null,
): PrimitiveTypeData {
    return PrimitiveTypeData(
        id = TypeId.build(T::class.qualifiedName!!),
        simpleName = T::class.simpleName!!,
        qualifiedName = T::class.qualifiedName!!,
        annotations = mutableListOf(
            AnnotationData(
                name = "type_format_annotation",
                values = mutableMapOf(
                    "type" to type,
                    "format" to format,
                    "example" to example,
                ),
                annotation = null,
            ),
        ),
    )
}