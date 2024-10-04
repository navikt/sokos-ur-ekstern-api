import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.jackson.jackson
import no.nav.sokos.customConfig

fun setupMockHttpClient(statusCode: HttpStatusCode = HttpStatusCode.OK): HttpClient {
    return HttpClient(MockEngine { request ->
        val content = String(request.body.toByteArray())
        when {
            content.contains("MANGLER") -> {
                respond(
                    content = requestManglerData,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    status = statusCode
                )
            }
            else -> {
                respond(
                    content = requestHarData,
                    headers = headersOf("Content-Type", ContentType.Application.Json.toString()),
                    status = statusCode
                )
            }
        }

    }) {
        expectSuccess = false
        install(ContentNegotiation) {
            jackson {
                customConfig()
            }
        }
    }
}


val requestHarData = """
    {
      "navurOppResv2OperationResponse": {
        "MHA2RESPONSE": {
          "response": {
            "correlationId": "string",
            "status": "OK",
            "statusMelding": "string",
            "resultatTabell": [
              {
                "mottakerId": "123",
                "datoPostert": "2024-09-25T21:00:00.000",
                "datoValutert": "2024-09-25T21:00:00.000",
                "rettighetshaver": "string",
                "ytelse": "string",
                "ytelseBeskrivelse": "string",
                "datoUtbetFom": "2024-09-25T21:00:00.000",
                "datoUtbetTom": "2024-09-25T21:00:00.000",
                "belop": 9999999999999.99,
                "typeUtbetaling": "s",
                "konto": "string",
                "kontoBeskrivelse": "string"
              }
            ]
          }
        }
      }
    }
""".trimIndent()

val requestManglerData = """
    {
      "navurOppResv2OperationResponse": {
        "MHA2RESPONSE": {
          "response": {
            "correlationId": "string",
            "status": "OK",
            "statusMelding": "string",
            "resultatTabell": [
              {
                "mottakerId": "123",
                "datoPostert": "",
                "datoValutert": "",
                "rettighetshaver": "",
                "ytelse": "",
                "ytelseBeskrivelse": "",
                "datoUtbetFom": "",
                "datoUtbetTom": "",
                "belop": 0,
                "typeUtbetaling": "",
                "konto": "",
                "kontoBeskrivelse": ""
              }
            ]
          }
        }
      }
    }
""".trimIndent()