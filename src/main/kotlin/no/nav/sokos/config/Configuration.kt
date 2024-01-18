package no.nav.sokos.config

import mu.KotlinLogging

private val logger = KotlinLogging.logger { }

data class Configuration(
    val useAuthentication: Boolean = readProperty("USE_AUTHENTICATION", default = "true") != "false",
//    val azureAd : AzureAd? = if (useAuthentication) AzureAd() else null,
) {
    data class AzureAd(
        val clientId: String = readProperty("AZURE_APP_CLIENT_ID"),
        val authorityEndpoint: String = readProperty("AZURE_APP_WELL_KNOWN_URL"),
        val tenant: String = readProperty("AZURE_APP_TENANT_ID"),
        val clientSecret: String = readProperty("AZURE_APP_CLIENT_SECRET"),
        val apiClientId: String = readProperty("API_CLIENT_ID")
    )
}

private fun readProperty(name: String, default: String? = null) =
    System.getenv(name)
        ?: System.getProperty(name)
        ?: default.takeIf { it != null }?.also { logger.warn { "Using default value for property $name" } }
        ?: throw RuntimeException("Mandatory property '$name' was not found")
