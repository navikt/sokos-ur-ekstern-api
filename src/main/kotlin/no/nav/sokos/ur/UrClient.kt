package no.nav.sokos.ur

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import no.nav.sokos.api.entitet.Mottaker
import no.nav.sokos.api.entitet.Periode
import no.nav.sokos.api.entitet.Ytelse
import no.nav.sokos.api.modell.FinnYtelser
import no.nav.sokos.config.Configuration
import no.nav.sokos.secureLogger
import no.nav.sokos.ur.entitet.FinnYtelserRequestContainer
import no.nav.sokos.ur.entitet.FinnYtelserRequestOperation
import no.nav.sokos.ur.entitet.MottakerIdTabell
import no.nav.sokos.ur.entitet.UrFinnYtelser
import no.nav.sokos.ur.entitet.UrFinnYtelserRequest
import no.nav.sokos.ur.entitet.UrFinnYtelserResponse
import no.nav.sokos.ur.entitet.YtelseTabell
import org.slf4j.MDC


private const val FEIL_VED_SJEKK_AV_ORGNUMMER = "OF"
private const val FEIL_YTELSESKODE_FOR_HJEMMEL = "YF"
private const val IKKE_OPPGITT_MOTTAKER = "IF"

class UrClient(
    private val urConfig: Configuration.UrConfig,
    private val client: HttpClient = urHttpClient(urConfig)
) {
    private val hentYtelserPath = "/navuroppresv2api/v2/finn-ytelser"

    suspend fun finnYtelser(finnYtelser: FinnYtelser): List<Mottaker> {
        val urRequest = UrFinnYtelserRequest(
            FinnYtelserRequestOperation(
                FinnYtelserRequestContainer(
                    UrFinnYtelser(
                        orgnr = finnYtelser.orgnummer,
                        correlationId = MDC.get("x-correlation-id"),
                        datoPostertFom = finnYtelser.fom,
                        datoPostertTom = finnYtelser.tom,
                        ytelseTabell = finnYtelser.ytelseskoder?.map { YtelseTabell(ytelsesKode = it) } ?: emptyList(),
                        mottakerIdTabell = finnYtelser.mottakere.map { MottakerIdTabell(mottakerId = it) },
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
            val responseData = body.operation.container.response
            when (responseData.status.uppercase()) {
                "OK" -> {
                    return responseData.resultatTabell.groupBy({ it.mottakerId }) { urYtelse ->
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

                FEIL_YTELSESKODE_FOR_HJEMMEL, FEIL_VED_SJEKK_AV_ORGNUMMER, IKKE_OPPGITT_MOTTAKER -> {
                    throw KlientFeil("Klientfeil: ${responseData.statusMelding}")
                }
            }
        }
        throw Exception("Feil fra stormaskin")
    }
}

class KlientFeil(val feilmelding: String) : Exception(feilmelding)