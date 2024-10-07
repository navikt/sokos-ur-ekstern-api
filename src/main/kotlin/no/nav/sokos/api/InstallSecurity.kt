package no.nav.sokos.api

import com.auth0.jwt.interfaces.Payload
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.Route
import mu.KotlinLogging
import no.nav.sokos.api.Sikkerhetskonfigurasjon.*
import no.nav.sokos.config.Configuration

private val logger = KotlinLogging.logger {}

enum class Sikkerhetskonfigurasjon{ AZUREAD, MASKINPORTEN }

fun Application.installSecurity(
    appConfig: Configuration
) {
    if (appConfig.useAuthentication) {
        logger.info("Running with authentication")
        install(Authentication) {
            jwt(MASKINPORTEN.name) {
                verifier(
                    appConfig.maskinportenServerConfig.jwkProvider,
                    appConfig.maskinportenServerConfig.issuer
                )
                {
                    acceptLeeway(1)
                }
                realm = appConfig.appName
                validate { credentials ->
                    try {
                        require(credentials.payload.scopes().contains("nav:reskontro:ytelser.read")) {
                            "Auth: Valid scope not found in claims".also { logger.info { it } }
                        }
                        JWTPrincipal(credentials.payload)
                    } catch (e: Throwable) {
                        null
                    }
                }
            }

            jwt(AZUREAD.name) {
                verifier(
                    appConfig.azureAdConfig.jwkProvider,
                    appConfig.azureAdConfig.openIdConfiguration.issuer
                ) {
                    acceptLeeway(1)
                }
                realm = appConfig.appName
                validate { credentials ->
                    try {
                        require(credentials.payload.audience.contains(appConfig.azureAdConfig.clientId)) {
                            "Auth: Valid audience not found in claims".also { logger.info { it } }
                        }
                        JWTPrincipal(credentials.payload)
                    } catch (e: Throwable) {
                        null
                    }
                }
            }
        }
    } else logger.warn { "Running WITHOUT authentication!" }
}

fun Route.authenticate(useAuthentication: Boolean, sikkerhetskonfigurasjon: String, block: Route.() -> Unit) {
    if (useAuthentication) authenticate(sikkerhetskonfigurasjon) { block() } else block()
}

private fun Payload.scopes(): List<String> = this.claims["scope"]?.asString()?.split(" ") ?: emptyList()