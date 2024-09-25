package no.nav.sokos

import mu.KotlinLogging
import no.nav.sokos.config.Configuration
import no.nav.sokos.ur.UrClient

val secureLogger = KotlinLogging.logger( "secureLogger")

fun main() {
    val appState = ApplicationState()
    val appConfig = Configuration()

    val urClient = UrClient(appConfig.urConfig)

    appState.ready = true
    HttpServer(appState, appConfig, urClient).start()
}
