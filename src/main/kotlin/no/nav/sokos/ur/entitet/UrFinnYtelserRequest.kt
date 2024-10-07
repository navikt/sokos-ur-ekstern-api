package no.nav.sokos.ur.entitet

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UrFinnYtelserRequest (
    @field:JsonProperty("navurOppResv2Operation")
    val operation: FinnYtelserRequestOperation
)

data class FinnYtelserRequestOperation (
    @field:JsonProperty("MHA2REQUEST")
    val container: FinnYtelserRequestContainer
)

data class FinnYtelserRequestContainer (
    val request: UrFinnYtelser
)

data class MottakerIdTabell (
    val mottakerId: String
)

data class YtelseTabell (
    val ytelsesKode: String
)

data class UrFinnYtelser (
    val orgnr: String,
    val correlationId: String,
    val datoPostertFom: LocalDate,
    val datoPostertTom: LocalDate,
    val ytelseTabell: List<YtelseTabell>,
    val mottakerIdTabell: List<MottakerIdTabell>,
    val bruker: String,
    val passord: String
)
