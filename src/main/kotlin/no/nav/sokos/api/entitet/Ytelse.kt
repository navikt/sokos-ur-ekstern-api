package no.nav.sokos.api.entitet

import java.math.BigDecimal
import java.time.LocalDate

class Ytelse(
    val datoPostert: LocalDate?,
    val datoValutert: LocalDate?,
    val rettighetshaver: String,
    val ytelse: String,
    val ytelseBeskrivelse: String,
    val ytelsePeriode: Periode,
    val belop: BigDecimal,
    val typeUtbetaling: String
)