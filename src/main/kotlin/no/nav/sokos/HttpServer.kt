package no.nav.sokos

import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.stop
import io.ktor.server.netty.Netty
import no.nav.sokos.api.installCommonFeatures
import no.nav.sokos.api.installSecurity
import no.nav.sokos.api.naisApi
import no.nav.sokos.api.swaggerApi
import no.nav.sokos.api.urEksternApi
import no.nav.sokos.config.Configuration
import no.nav.sokos.ur.UrClient
import java.util.concurrent.TimeUnit

class HttpServer(
    private val appState: ApplicationState,
    appConfig: Configuration,
    urClient: UrClient,
    port: Int = 8080,
) {

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            appState.running = false
            this.embeddedServer.stop(2, 5, TimeUnit.SECONDS)
        })
    }

    private val embeddedServer = embeddedServer(Netty, port) {
        installCommonFeatures()
        installSecurity(appConfig)
        naisApi({ appState.ready }, { appState.running })
        urEksternApi(appConfig.useAuthentication, urClient)
        swaggerApi()
    }

    fun start() {
        embeddedServer.start(wait = true)
    }
}

