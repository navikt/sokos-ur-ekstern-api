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
import no.nav.sokos.api.entitet.Mottaker
import no.nav.sokos.api.entitet.Periode
import no.nav.sokos.api.entitet.Ytelse
import java.math.BigDecimal
import java.time.LocalDate


private val logger = KotlinLogging.logger { }

fun Application.urEksternApi(
    useAuthentication: Boolean = true
) {

    routing {
        authenticate(useAuthentication) {
            route("ur-ekstern/api") {
                post("v1/finn-ytelser") {
                    val req: FinnYtelserRequest = call.receive()
                    val response = req.mottakere.map {
                        Mottaker(it, listOf(Ytelse(
                            datoPostert = LocalDate.now(),
                            datoValutert = LocalDate.now(),
                            rettighetshaver = "rettighetshaver",
                            ytelse = "AAP",
                            ytelseBeskrivelse = "ytelseBeskrivelse",
                            ytelsePeriode = Periode(
                                fom = LocalDate.now(), tom = LocalDate.now()
                            ),
                            belop = BigDecimal.ONE,
                            typeUtbetaling = "typeUtbetaling"
                        )))
                    }
                    call.respond(response)
                }
            }
        }
    }
}
