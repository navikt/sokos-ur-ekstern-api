package no.nav.sokos.api.entitet

class FinnYtelserRequest (
    val periode: Periode,
    val mottakere  : List<String>,
    val ytelseskoder: List<String>
)
