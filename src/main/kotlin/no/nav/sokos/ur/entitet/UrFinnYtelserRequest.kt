package no.nav.sokos.ur.entitet

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class UrFinnYtelserRequest (
    val navurOppResv1Operation: FinnYtelserRequestOperation
)

data class FinnYtelserRequestOperation (
    @field:JsonProperty("MHA1REQUEST")
    val MHA1REQUEST: FinnYtelserRequestContainer
)

data class FinnYtelserRequestContainer (
    val request: FinnYtelser
)

data class MottakerIdTabell (
    val mottakerId: String
)

data class YtelseTabell (
    val ytelsesKode: String
)

data class FinnYtelser (
    val orgnr: String,
    val correlationId: String,
    val hjemmelshaver: String,
    val datoPostertFom: LocalDate,
    val datoPostertTom: LocalDate,
    val ytelseTabell: List<YtelseTabell>,
    val mottakerIdTabell: List<MottakerIdTabell>,
    val bruker: String,
    val passord: String
)
