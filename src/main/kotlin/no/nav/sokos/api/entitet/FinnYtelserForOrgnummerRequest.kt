package no.nav.sokos.api.entitet

class FinnYtelserForOrgnummerRequest (
    val periode: Periode,
    val mottakere  : List<String>,
    val ytelseskoder: List<String>?,
    val orgnummer: String
)
