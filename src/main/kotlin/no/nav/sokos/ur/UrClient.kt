package no.nav.sokos.ur

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import mu.KotlinLogging
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.entitet.Mottaker
import no.nav.sokos.api.entitet.Periode
import no.nav.sokos.api.entitet.Ytelse
import no.nav.sokos.config.Configuration
import no.nav.sokos.secureLogger
import no.nav.sokos.ur.entitet.FinnYtelser
import no.nav.sokos.ur.entitet.FinnYtelserRequestContainer
import no.nav.sokos.ur.entitet.FinnYtelserRequestOperation
import no.nav.sokos.ur.entitet.MottakerIdTabell
import no.nav.sokos.ur.entitet.UrFinnYtelserRequest
import no.nav.sokos.ur.entitet.UrFinnYtelserResponse
import no.nav.sokos.ur.entitet.YtelseTabell


class UrClient(
    private val urConfig: Configuration.UrConfig,
    private val client: HttpClient = urHttpClient(urConfig)
) {
    private val hentYtelserPath = "/navuroppresv1api/v1/finn-ytelser"

    suspend fun finnYtelser(orgnr: String, correlationId: String, request: FinnYtelserRequest): List<Mottaker> {
        val urRequest = UrFinnYtelserRequest(
            FinnYtelserRequestOperation(
                FinnYtelserRequestContainer(
                    FinnYtelser(
                        orgnr = orgnr,
                        correlationId = correlationId,
                        hjemmelshaver = "husbanKEN",
                        datoPostertFom = request.periode.fom,
                        datoPostertTom = request.periode.tom,
                        ytelseTabell = request.ytelseskoder?.map { YtelseTabell(ytelsesKode = it) } ?: emptyList(),
                        mottakerIdTabell = request.mottakere.map { MottakerIdTabell(mottakerId = it) },
                        bruker = urConfig.username,
                        passord = urConfig.password
                    )
                )
            )
        )

        secureLogger.info { "Request: $urRequest" }

        val response = client.post(urConfig.endpointUrl + hentYtelserPath) {
            contentType(ContentType.Application.Json)
            setBody(urRequest)
        }

        secureLogger.info("Response: ${response.bodyAsText()}")

        if (response.status.isSuccess()) {
            val body = response.body<UrFinnYtelserResponse>()
            secureLogger.info { body }
            val responseData = body.navurOppResv1OperationResponse.MHA1RESPONSE.response
            if (responseData.status.uppercase() == "OK") {
                return responseData.resultatTabell.groupBy({it.mottakerId}) { urYtelse ->
                    if (urYtelse.ytelse.isNotBlank()) {
                        Ytelse(
                            datoValutert = urYtelse.datoValutert!!,
                            datoPostert = urYtelse.datoPostert!!,
                            rettighetshaver = urYtelse.rettighetshaver,
                            ytelse = urYtelse.ytelse,
                            ytelseBeskrivelse = urYtelse.ytelseBeskrivelse,
                            belop = urYtelse.belop,
                            ytelsePeriode = Periode(urYtelse.datoUtbetFom!!, urYtelse.datoUtbetTom!!),
                            typeUtbetaling = urYtelse.typeUtbetaling.takeIf { it.isNotBlank() }
                        )
                    } else null
                }.map { (k, v) -> Mottaker(k, v.filterNotNull()) }
            }
        }
        throw Exception("Feil fra stormaskin")
    }
}
