package no.nav.sokos.api

import com.auth0.jwt.interfaces.Claim
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.secureLogger
import no.nav.sokos.ur.UrClient
import org.slf4j.MDC

fun Application.urEksternApi(
    useAuthentication: Boolean = true,
    urClient: UrClient
) {
    routing {
        authenticate(useAuthentication) {
            route("ur-ekstern/api") {
                post("v1/finn-ytelser") {
                    val orgnr = call.hentHjemmelshaver(useAuthentication) ?: "TEST" //TODO Default
                    val correlationId = MDC.get("x-correlation-id")
                    secureLogger.info { "$orgnr har gjort et kall" }

                    val request: FinnYtelserRequest = call.receive()
                    val response = urClient.finnYtelser(orgnr, correlationId, request)
                    call.respond(response)
                }
            }
        }
    }
}

private fun ApplicationCall.hentHjemmelshaver(useAuthentication: Boolean): String? {
    if(useAuthentication){
        val consumer: Claim? = this.authentication.principal<JWTPrincipal>()!!.payload.claims["consumer"]
        val consumerId = consumer?.asMap()?.get("ID")?.toString()?.split(":")?.last()
        return consumerId
    } else return null
}
