package no.nav.sokos.api.modell

import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.api.entitet.FinnYtelserRequest
import java.time.LocalDate

class FinnYtelser(
    val orgnummer: String,
    val fom: LocalDate,
    val tom: LocalDate,
    val mottakere: List<String>,
    val ytelseskoder: List<String>?
) {
    constructor(request: FinnYtelserForOrgnummerRequest) : this(
        request.orgnummer,
        request.periode.fom,
        request.periode.tom,
        request.mottakere,
        request.ytelseskoder
    )

    constructor(orgnummer: String, request: FinnYtelserRequest) : this(
        orgnummer,
        request.periode.fom,
        request.periode.tom,
        request.mottakere,
        request.ytelseskoder
    )
}