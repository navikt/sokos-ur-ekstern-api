package no.nav.sokos

import no.nav.sokos.config.Configuration

fun main() {
    val appState = ApplicationState()
    val appConfig = Configuration()

    appState.ready = true
    HttpServer(appState, appConfig).start()
}