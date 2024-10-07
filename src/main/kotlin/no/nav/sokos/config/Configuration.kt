package no.nav.sokos.config

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.annotation.JsonProperty
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.sokos.defaultHttpClient
import java.net.URI
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger { }

data class Configuration(
    val appName: String = readProperty("NAIS_APP_NAME"),
    val useAuthentication: Boolean = readProperty("USE_AUTHENTICATION", default = "true") != "false",
    val maskinportenServerConfig: MaskinportenServerConfig = MaskinportenServerConfig(),
    val azureAdConfig: AzureAdConfig = AzureAdConfig(),
    val urConfig: UrConfig = UrConfig()
) {
    data class UrConfig(
        val endpointUrl: String = readProperty("UR_ENDPOINT_URL", ""),
        val username: String = readProperty("UR_USERNAME", ""),
        val password: String = readProperty("UR_PASSWORD", ""),
        val trustStore: String = readProperty("NAV_TRUSTSTORE_PATH", ""),
        val trustStorePassword: String = readProperty("NAV_TRUSTSTORE_PASSWORD", ""),
    )

    data class AzureAdConfig(
        val clientId: String = readProperty("AZURE_APP_CLIENT_ID", ""),
        val authorityEndpoint: String = readProperty("AZURE_APP_WELL_KNOWN_URL", "")
    ){
        val openIdConfiguration: OpenIdConfiguration by lazy {
            runBlocking { defaultHttpClient.get(authorityEndpoint).body() }
        }
        val jwkProvider: JwkProvider by lazy {
            JwkProviderBuilder(URI(openIdConfiguration.jwksUri).toURL())
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10,1, TimeUnit.MINUTES)
                .build()
        }
    }


    data class OpenIdConfiguration(
        @JsonProperty("jwks_uri") val jwksUri: String,
        @JsonProperty("issuer") val issuer: String,
        @JsonProperty("token_endpoint") val tokenEndpoint: String,
        @JsonProperty("authorization_endpoint") val authorizationEndpoint: String
    )



    data class MaskinportenServerConfig(
        val authorityEndpoint: String = readProperty("MASKINPORTEN_WELL_KNOWN_URL", ""),
        val issuer: String = readProperty("MASKINPORTEN_ISSUER", ""),
        val jwksUri: String = readProperty("MASKINPORTEN_JWKS_URI", "")
    ) {
        val jwkProvider: JwkProvider by lazy {
            JwkProviderBuilder(URI(jwksUri).toURL())
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build()
        }
    }
}

private fun readProperty(name: String, default: String? = null) =
    System.getenv(name)
        ?: System.getProperty(name)
        ?: default.takeIf { it != null }?.also { logger.warn { "Using default value for property $name" } }
        ?: throw RuntimeException("Mandatory property '$name' was not found")
