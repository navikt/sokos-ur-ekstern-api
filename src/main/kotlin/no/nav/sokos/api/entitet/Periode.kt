package no.nav.sokos.api.entitet

import java.time.LocalDate

data class Periode(
    val fom: LocalDate,
    val tom: LocalDate,
)