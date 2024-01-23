package no.nav.sokos.api

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.routing.Route
import mu.KotlinLogging
import no.nav.sokos.config.Configuration

private val logger = KotlinLogging.logger {}

fun Application.installSecurity(
    appConfig: Configuration
) {
    if (appConfig.useAuthentication) {
        logger.info("Running with authentication")
        install(Authentication) {
            jwt {
                this.verifier( //TODO Verifiser at denne gjør signature- og issuer-validation
                    appConfig.maskinportenServerConfig.jwkProvider,
                    appConfig.maskinportenServerConfig.issuer
                )
                realm = appConfig.appName
                validate { credentials ->
                    try {
                        val scopes = credentials.payload.claims["scope"]?.asString()?.split(" ") ?: emptyList()
                        require(scopes.contains("nav:reskontro:ytelser.read")) { // TODO Finn ut hvordan vi kan hente ut scope fra NAIS/Maskinporten
                            "Auth: Valid scope not found in claims".also { logger.info { it } }
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

fun Route.authenticate(useAuthentication: Boolean, block: Route.() -> Unit) {
    if (useAuthentication) authenticate { block() } else block()
}
