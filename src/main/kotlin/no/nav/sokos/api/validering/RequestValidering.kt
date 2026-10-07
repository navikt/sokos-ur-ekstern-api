package no.nav.sokos.api.validering

import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.entitet.Periode

const val MAKS_ANTALL_MOTTAKERE = 1000
const val MAKS_ANTALL_YTELSESKODER = 30

fun FinnYtelserRequest.validerRequest(): List<String> =
    validerPeriode(periode)+ validerMottakere(mottakere) + validerYtelseskoder(ytelseskoder)

fun FinnYtelserForOrgnummerRequest.validerRequest(): List<String> =
    validerOrgnummer(orgnummer) + validerPeriode(periode) + validerMottakere(mottakere) + validerYtelseskoder(ytelseskoder)

private fun validerOrgnummer(orgnummer: String): List<String> =
    if (orgnummer.length != 9) {
        listOf("Orgnummer må bestå av nøyaktig 9 siffer.")
    } else {
        emptyList()
    }

private fun validerPeriode(periode: Periode): List<String> =
    if (periode.fom.isAfter(periode.tom)) {
        listOf("Ugyldig periode: fom kan ikke være etter tom.")
    } else {
        emptyList()
    }

private fun validerMottakere(mottakere: List<String>): List<String> {
    if (mottakere.isEmpty()) return emptyList()

    return buildList {
        if (mottakere.size > MAKS_ANTALL_MOTTAKERE) {
            add("Maks antall mottakere i en request er $MAKS_ANTALL_MOTTAKERE.")
        }

        if (mottakere.any { it.length != 11 }) {
            add("Alle mottakere må bestå av nøyaktig 11 siffer.")
        }
    }
}

private fun validerYtelseskoder(ytelseskoder: List<String>?): List<String> {
    if (ytelseskoder == null) return emptyList()

    return buildList {
        if (ytelseskoder.size > MAKS_ANTALL_YTELSESKODER) {
            add("Maks antall ytelseskoder i en request er $MAKS_ANTALL_YTELSESKODER.")
        }
        if (ytelseskoder.any { it.isBlank() }) {
            add("Ytelseskoder kan ikke være blanke.")
        }
    }
}
