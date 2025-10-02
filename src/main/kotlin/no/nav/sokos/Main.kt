package no.nav.sokos

import no.nav.sokos.config.Configuration
import no.nav.sokos.ur.UrClient

fun main() {
    val appState = ApplicationState()
    val appConfig = Configuration()

    val urClient = UrClient(appConfig.urConfig)

    appState.ready = true
    HttpServer(appState, appConfig, urClient).start()
}
