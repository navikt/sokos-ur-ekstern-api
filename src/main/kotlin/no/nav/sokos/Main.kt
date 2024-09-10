package no.nav.sokos

import mu.KotlinLogging
import no.nav.sokos.config.Configuration

val secureLogger = KotlinLogging.logger { "secureLogger"}

fun main() {
    val appState = ApplicationState()
    val appConfig = Configuration()

    appState.ready = true
    HttpServer(appState, appConfig).start()
}
