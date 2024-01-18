package no.nav.sokos.api

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.prometheus.client.exporter.common.TextFormat
import no.nav.sokos.metrics.Metrics

fun Application.naisApi(alive: () -> Boolean, ready: () -> Boolean) {
    routing {
        route("internal") {
            get("is_alive") {
                when (alive()) {
                    true -> call.respondText { "Application is alive" }
                    else -> call.respondText(
                        text = "Application is not alive",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
            get("is_ready") {
                when (ready()) {
                    true -> call.respondText { "Application is ready" }
                    else -> call.respondText(
                        text = "Application is not ready",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
            get("metrics") {
                call.respondText(ContentType.parse(TextFormat.CONTENT_TYPE_004)) { Metrics.prometheusRegistry.scrape() }
            }
        }
    }
}

