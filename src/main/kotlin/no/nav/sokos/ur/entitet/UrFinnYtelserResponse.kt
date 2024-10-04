package no.nav.sokos.ur.entitet

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class UrFinnYtelserResponse (
    @field:JsonProperty("navurOppResv2OperationResponse")
    val operation: FinnYtelserResponseOperation
)

data class FinnYtelserResponseOperation (
    @field:JsonProperty("MHA2RESPONSE")
    val container: FinnYtelserResponseContainer
)

data class FinnYtelserResponseContainer (
    val response: FinnYtelserResponseData
)

data class FinnYtelserResponseData (
    val correlationId: String,
    val status: String,
    val statusMelding: String,
    val resultatTabell: List<UrYtelse> = emptyList()
)

data class UrYtelse (
    val mottakerId: String,
    val datoPostert: LocalDate?,
    val datoValutert: LocalDate?,
    val rettighetshaver: String,
    val ytelse: String,
    val ytelseBeskrivelse: String,
    val datoUtbetFom: LocalDate?,
    val datoUtbetTom: LocalDate?,
    val belop: BigDecimal,
    val typeUtbetaling: String,
    val konto: String,
    val kontoBeskrivelse: String
)
