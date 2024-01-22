package no.nav.sokos.api

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.swaggerApi() {
    routing {
        staticResources("/api/v1/docs/", "api/sokos_ur_ekstern") {
            default("index.html")
        }
    }
}
