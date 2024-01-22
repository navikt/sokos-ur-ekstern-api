package no.nav.sokos.api

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import mu.KotlinLogging
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.entitet.Utbetaling


val logger = KotlinLogging.logger {  }

fun Application.urEksternApi(
    brukAutentisering: Boolean = true
) {

    routing {
        route("ur-ekstern/api") {
            post("v1/finn-ytelser") {
                val req: FinnYtelserRequest = call.receive()
                call.respond(req.fnrEllerOrgnr.map { Utbetaling(it) })
            }
        }
    }
}