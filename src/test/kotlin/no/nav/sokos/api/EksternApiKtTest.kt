package no.nav.sokos.api

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.restassured.RestAssured
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.Header
import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.api.entitet.Mottaker
import no.nav.sokos.api.entitet.Periode
import no.nav.sokos.config.Configuration
import no.nav.sokos.jsonMapper
import no.nav.sokos.ur.UrClient
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
    fun urEksternApiHarData() {
        val apiResponse = RestAssured.given()
            .filter(validationFilter)
            .header(Header("Content-Type", "application/json"))
            .header(Header("x-correlation-id", "3"))
            .body(FinnYtelserRequest(
                periode = Periode(LocalDate.now(), LocalDate.now()),
                ytelseskoder = listOf("AAP"),
                mottakere = listOf("123")
            ))
            .post("/ur-ekstern/api/v1/finn-ytelser")

        apiResponse.then().statusCode(200)

        assertEquals("123", jsonMapper.readValue<List<Mottaker>>(apiResponse.body.asString())[0].mottakerId)
    }

    @Test
    fun urEksternApiManglerData() {
        val apiResponse = RestAssured.given()
            .filter(validationFilter)
            .header(Header("Content-Type", "application/json"))
            .header(Header("x-correlation-id", "3"))
            .body(FinnYtelserRequest(
                periode = Periode(LocalDate.now(), LocalDate.now()),
                ytelseskoder = listOf("AAP"),
                mottakere = listOf("MANGLER")
            ))
            .post("/ur-ekstern/api/v1/finn-ytelser")

        apiResponse.then().statusCode(200)

        assertEquals("123", jsonMapper.readValue<List<Mottaker>>(apiResponse.body.asString())[0].mottakerId)
    }

    @Test
    fun urEksternApiMedOrgnummerRequestHarData() {
        val apiResponse = RestAssured.given()
            .filter(validationFilter)
            .header(Header("Content-Type", "application/json"))
            .header(Header("x-correlation-id", "3"))
            .body(FinnYtelserForOrgnummerRequest(
                periode = Periode(LocalDate.now(), LocalDate.now()),
                ytelseskoder = listOf("AAP"),
                mottakere = listOf("123"),
                orgnummer = "orgnr"
            ))
            .post("/ur-ekstern/api/v1/finn-ytelser-for-orgnummer")

        apiResponse.then().statusCode(200)

        assertEquals("123", jsonMapper.readValue<List<Mottaker>>(apiResponse.body.asString())[0].mottakerId)
    }


    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
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
    }
}
