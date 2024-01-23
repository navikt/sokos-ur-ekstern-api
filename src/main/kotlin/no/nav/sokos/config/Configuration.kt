package no.nav.sokos.config

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jose.jwk.RSAKey
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
    val azureAdConfig : AzureAd? = if (useAuthentication) AzureAd() else null,
    val maskinportenClientConfig: MaskinportenClientConfig = MaskinportenClientConfig(),
    val maskinportenServerConfig: MaskinportenServerConfig = MaskinportenServerConfig()
) {
    data class AzureAd(
        val clientId: String = readProperty("AZURE_APP_CLIENT_ID"),
        val authorityEndpoint: String = readProperty("AZURE_APP_WELL_KNOWN_URL"),
        val tenant: String = readProperty("AZURE_APP_TENANT_ID"),
        val clientSecret: String = readProperty("AZURE_APP_CLIENT_SECRET"),
        val apiClientId: String = readProperty("API_CLIENT_ID")
    )

    data class MaskinportenClientConfig(
        val clientId: String = readProperty("MASKINPORTEN_CLIENT_ID", ""),
        val authorityEndpoint: String = readProperty("MASKINPORTEN_WELL_KNOWN_URL", ""),
        val rsaKey: RSAKey? = RSAKey.parse(readProperty("MASKINPORTEN_CLIENT_JWK", "")),
        val scopes: String = readProperty("MASKINPORTEN_SCOPES", ""),
    ) : JwtConfig(authorityEndpoint)

    data class MaskinportenServerConfig(
        val authorityEndpoint: String = readProperty("MASKINPORTEN_WELL_KNOWN_URL", ""),
        val issuer: String = readProperty("MASKINPORTEN_ISSUER"),
        val jwksUri: String = readProperty("MASKINPORTEN_JWKS_URI")
    ) {
        val jwkProvider: JwkProvider by lazy {
            JwkProviderBuilder(URI(jwksUri).toURL())
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build()
        }
    }

    data class OpenIdConfiguration(
        @JsonProperty("jwks_uri") val jwksUri: String,
        @JsonProperty("issuer") val issuer: String,
        @JsonProperty("token_endpoint") val tokenEndpoint: String,
    )

    open class JwtConfig(wellKnownUrl: String) {
        val openIdConfiguration: OpenIdConfiguration by lazy {
            runBlocking { defaultHttpClient.get(wellKnownUrl).body() }
        }
        val jwkProvider: JwkProvider by lazy {
            JwkProviderBuilder(URI(openIdConfiguration.jwksUri).toURL())
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10,1, TimeUnit.MINUTES)
                .build()
        }
    }
}

private fun readProperty(name: String, default: String? = null) =
    System.getenv(name)
        ?: System.getProperty(name)
        ?: default.takeIf { it != null }?.also { logger.warn { "Using default value for property $name" } }
        ?: throw RuntimeException("Mandatory property '$name' was not found")
