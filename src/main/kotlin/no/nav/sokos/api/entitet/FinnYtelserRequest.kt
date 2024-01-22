package no.nav.sokos.api.entitet

import java.time.LocalDate

class FinnYtelserRequest (
    val fom: LocalDate,
    val tom: LocalDate,
    val fnrEllerOrgnr: List<String>
)
