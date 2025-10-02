package no.nav.sokos.api

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.Header
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.entitet.Mottaker
import no.nav.sokos.api.entitet.Periode
import no.nav.sokos.config.Configuration
import no.nav.sokos.jsonMapper
import no.nav.sokos.ur.UrClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import setupMockHttpClient
import java.time.LocalDate

private const val PORT = 21212
private const val URL = "http://localhost"

class EksternApiKtTest {

    private val validationFilter = OpenApiValidationFilter("spec/ur-ekstern-api-v1-openapi-spec.yaml")

    @Test
    fun `ur-ekstern-api har data`() {
        Given {
            filter(validationFilter)
            header(Header("Content-Type", "application/json"))
            header(Header("x-correlation-id", "3"))
            body(
                FinnYtelserRequest(
                    periode = Periode(LocalDate.now(), LocalDate.now()),
                    ytelseskoder = listOf("AAP"),
                    mottakere = listOf("123")
                )
            )
        } When {
            post("/ur-ekstern/api/v1/finn-ytelser")
        } Then {
            statusCode(200)

            bodyAs<List<Mottaker>> { mottakerList ->
                assertEquals(1, mottakerList.size)
                assertEquals("123", mottakerList[0].mottakerId)
                assertEquals(2, mottakerList[0].ytelser.size)
            }
        }
    }

    @Test
    fun `ur-ekstern-api mangler data`() {
        Given {
            filter(validationFilter)
            header(Header("Content-Type", "application/json"))
            header(Header("x-correlation-id", "3"))
            body(
                FinnYtelserRequest(
                    periode = Periode(LocalDate.now(), LocalDate.now()),
                    ytelseskoder = listOf("AAP"),
                    mottakere = listOf("MANGLER")
                )
            )
        } When {
            post("/ur-ekstern/api/v1/finn-ytelser")
        } Then {
            statusCode(200)

            bodyAs<List<Mottaker>> { mottakerList ->
                assertEquals(1, mottakerList.size)
                assertEquals("123", mottakerList[0].mottakerId)
                assertEquals(0, mottakerList[0].ytelser.size)
            }
        }
    }

    @Test
    fun `ur-ekstern-api request mangler hjemmel`() {
        Given {
            filter(validationFilter)
            header(Header("Content-Type", "application/json"))
            header(Header("x-correlation-id", "3"))
            body(
                FinnYtelserRequest(
                    periode = Periode(LocalDate.now(), LocalDate.now()),
                    ytelseskoder = listOf("AAP"),
                    mottakere = listOf("FEIL")
                )
            )
        } When {
            post("/ur-ekstern/api/v1/finn-ytelser")
        } Then {
            statusCode(403)
            assertEquals("Klientfeil: Feil ytelsekode for hjemmel", extract().body().asString())
        }
    }

    @Test
    fun `ur-ekstern-api med orgnummer request har data`() {
        Given {
            filter(validationFilter)
            header(Header("Content-Type", "application/json"))
            header(Header("x-correlation-id", "3"))
            body(
                FinnYtelserForOrgnummerRequest(
                    periode = Periode(LocalDate.now(), LocalDate.now()),
                    ytelseskoder = listOf("AAP"),
                    mottakere = listOf("123"),
                    orgnummer = "orgnr"
                )
            )
        } When {
            post("/ur-ekstern/api/v1/finn-ytelser-for-orgnummer")
        } Then {
            statusCode(200)
            assertEquals("123", jsonMapper.readValue<List<Mottaker>>(extract().body().asString())[0].mottakerId)
        }
    }

    private inline fun <reified T> ValidatableResponse.bodyAs(block: (T) -> Unit) =
        block(jsonMapper.readValue<T>(extract().body().asString()))


    companion object {
        private lateinit var embeddedServer: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>

        @JvmStatic
        @BeforeAll
        fun init() {
            embeddedServer =
                embeddedServer(Netty, PORT) {
                    installCommonFeatures()
                    urEksternApi(false, UrClient(Configuration.UrConfig(), setupMockHttpClient()))
                }.start()

            RestAssured.baseURI = URL
            RestAssured.basePath = ""
            val port = PORT
            RestAssured.port = port
            RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                ObjectMapperConfig().jackson2ObjectMapperFactory { _, _ -> jsonMapper }
            )
        }

        @JvmStatic
        @AfterAll
        fun stopServer() {
            RestAssured.reset()
            embeddedServer.stop(gracePeriodMillis = 2000, timeoutMillis = 5000)
        }
    }
}
