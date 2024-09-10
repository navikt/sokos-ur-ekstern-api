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
import no.nav.sokos.api.entitet.Mottaker
import no.nav.sokos.api.entitet.Periode
import no.nav.sokos.api.entitet.Ytelse
import no.nav.sokos.secureLogger
import java.math.BigDecimal
import java.time.LocalDate

fun Application.urEksternApi(
    useAuthentication: Boolean = true
) {

    routing {
        authenticate(useAuthentication) {
            route("ur-ekstern/api") {
                post("v1/finn-ytelser") {
                    val hjemmelshaver = call.hentHjemmelshaver(useAuthentication)
                    secureLogger.info { "$hjemmelshaver har gjort et kall" }

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

private fun ApplicationCall.hentHjemmelshaver(useAuthentication: Boolean): String? {
    if(useAuthentication){
        val consumer: Claim? = this.authentication.principal<JWTPrincipal>()!!.payload.claims["consumer"]
        val consumerId = consumer?.asMap()?.get("ID")?.toString()?.split(":")?.last()
        return consumerId
    } else return null
}
