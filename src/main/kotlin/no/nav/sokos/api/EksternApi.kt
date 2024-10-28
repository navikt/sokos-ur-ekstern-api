package no.nav.sokos.api

import com.auth0.jwt.interfaces.Claim
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import no.nav.sokos.api.Sikkerhetskonfigurasjon.AZUREAD
import no.nav.sokos.api.Sikkerhetskonfigurasjon.MASKINPORTEN
import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.modell.FinnYtelser
import no.nav.sokos.metrics.Metrics
import no.nav.sokos.secureLogger
import no.nav.sokos.ur.KlientFeil
import no.nav.sokos.ur.UrClient

fun Application.urEksternApi(
    useAuthentication: Boolean = true,
    urClient: UrClient
) {
    routing {
        authenticate(useAuthentication, MASKINPORTEN.name) {
            route("ur-ekstern/api") {
                post("v1/finn-ytelser") {
                    try {
                        val orgnr = if (useAuthentication) call.hentHjemmelshaver()!! else "TEST"
                        secureLogger.info { "$orgnr har gjort et kall" }
                        val request: FinnYtelserRequest = call.receive()
                        if (request.mottakere.size > 1000) {
                            call.respond(HttpStatusCode.BadRequest, "Maks antall mottakere i en request er 1000.")
                        } else {
                            incrementYtelsestyeMetrikker(request.ytelseskoder, orgnr)
                            call.respond(urClient.finnYtelser(FinnYtelser(orgnr, request)))
                        }
                    } catch (e: Exception) {
                        if (e is KlientFeil) {
                            secureLogger.warn { "klientfeil ${e.feilmelding}" }
                            call.respond(HttpStatusCode.Forbidden, e.feilmelding)
                        } else {
                            secureLogger.error(e) { "Noe gikk galt" }
                            call.respond(HttpStatusCode.InternalServerError, "noe gikk galt")
                        }
                    }
                }
            }
        }
        authenticate(useAuthentication, AZUREAD.name) {
            route("ur-ekstern/api") {
                post("v1/finn-ytelser-for-orgnummer") {
                    val kallendeSystem = call.hentKallendeSystem()
                    secureLogger.info("$kallendeSystem har gjort en request: ${call.receiveText()}")
                    val request: FinnYtelserForOrgnummerRequest = call.receive()
                    try {
                        val orgnr = request.orgnummer
                        if (request.mottakere.size > 1000) {
                            call.respond(HttpStatusCode.BadRequest, "Maks antall mottakere i en request er 1000.")
                        } else {
                            incrementYtelsestyeMetrikker(request.ytelseskoder, orgnr)
                            call.respond(urClient.finnYtelser(FinnYtelser(request)))
                        }
                    } catch (e: Exception) {
                        if (e is KlientFeil) {
                            secureLogger.warn { "klientfeil ${e.feilmelding}" }
                            call.respond(HttpStatusCode.Forbidden, e.feilmelding)
                        } else {
                            secureLogger.error(e) { "Noe gikk galt" }
                            call.respond(HttpStatusCode.InternalServerError, "noe gikk galt")
                        }
                    }
                }
            }
        }
    }
}

private fun incrementYtelsestyeMetrikker(ytelesesKoder: List<String>?, orgnr: String) {
    (ytelesesKoder ?: listOf("ALLE")).forEach {
        Metrics.ytelsestypeCounter(orgnr, it).increment()
    }
}

fun ApplicationCall.hentHjemmelshaver(): String? {
    val consumer: Claim? = this.authentication.principal<JWTPrincipal>()?.payload?.claims?.get("consumer")
    val consumerId = consumer?.asMap()?.get("ID")?.toString()?.split(":")?.last()
    return consumerId

}

fun ApplicationCall.hentKallendeSystem(): String? {
    return authentication.principal<JWTPrincipal>()?.payload?.claims?.get("azp_name")?.asString()
}
