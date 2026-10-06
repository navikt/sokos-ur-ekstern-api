package no.nav.sokos.api.validering

import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.entitet.Periode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

private const val GYLDIG_MOTTAKER = "12345678901"
private const val GYLDIG_ORGNUMMER = "889640782"

class RequestValideringTest {

    private val periodeLikDag = Periode(LocalDate.now(), LocalDate.now())
    private val periodeUgyldig = Periode(LocalDate.now(), LocalDate.now().minusDays(7))
    private val periodeGyldig = Periode(LocalDate.now().minusDays(7), LocalDate.now())

    private fun request(
        periode: Periode = periodeLikDag,
        mottakere: List<String> = listOf(GYLDIG_MOTTAKER),
        ytelseskoder: List<String>? = listOf("AAP"),
    ) = FinnYtelserRequest(periode, mottakere, ytelseskoder)

    private fun orgnummerRequest(orgnummer: String) =
        FinnYtelserForOrgnummerRequest(periodeLikDag, listOf(GYLDIG_MOTTAKER), null, orgnummer)

    @Test
    fun `gyldig request gir ingen feil`() {
        assertTrue(request().validerRequest().isEmpty())
        assertTrue(request(ytelseskoder = null).validerRequest().isEmpty())
        assertTrue(request(ytelseskoder = emptyList()).validerRequest().isEmpty())
        assertTrue(orgnummerRequest(GYLDIG_ORGNUMMER).validerRequest().isEmpty())
    }

    @Test
    fun `tom mottakerliste er lov`() {
        assertTrue(request(mottakere = emptyList()).validerRequest().isEmpty())
    }

    @Test
    fun `mer enn 1000 unike mottakere gir feil`() {
        val mottakere = (1..1001).map { it.toString().padStart(11, '0') }
        assertEquals(listOf("Maks antall mottakere i en request er 1000."), request(mottakere = mottakere).validerRequest())
    }

    @Test
    fun `duplikate mottakere telles ikke mot maksgrensen`() {
        val mottakere = (1..1000).map { it.toString().padStart(11, '0') } + "00000000001"
        assertTrue(request(mottakere = mottakere).validerRequest().isEmpty())
    }

    @Test
    fun `fom etter tom gir feil`() {
        assertEquals(
            listOf("Ugyldig periode: fom kan ikke være etter tom."),
            request(periode = periodeUgyldig).validerRequest()
        )
    }

    @Test
    fun `fom lik tom er gyldig`() {
        assertTrue(request(periode = periodeLikDag).validerRequest().isEmpty())
    }

    @Test
    fun `tom etter fom er gyldig`() {
        assertTrue(request(periode = periodeGyldig).validerRequest().isEmpty())
    }

    @Test
    fun `for mange ytelseskoder gir feil`() {
        assertEquals(
            listOf("Maks antall ytelseskoder i en request er 30."),
            request(ytelseskoder = List(31) { "AAP" }).validerRequest(),
        )
    }

    @Test
    fun `blank ytelseskode gir feil`() {
        assertEquals(listOf("Ytelseskoder kan ikke være blanke."), request(ytelseskoder = listOf("AAP", " ")).validerRequest())
    }

    @Test
    fun `feil lengde på orgnummer gir feil`() {
        listOf("", "orgnr", "88964078", "8896407821").forEach {
            val request: FinnYtelserForOrgnummerRequest = orgnummerRequest(it)
            assertEquals(listOf("Orgnummer må bestå av nøyaktig 9 siffer."), request.validerRequest(), it)
        }
    }

    @Test
    fun `returnerer flere feil`() {
        val feil = FinnYtelserForOrgnummerRequest(
            periode = periodeUgyldig,
            mottakere = listOf("123"),
            ytelseskoder = listOf(""),
            orgnummer = "123",
        ).validerRequest()
        assertEquals(4, feil.size)
    }
}
