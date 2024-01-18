package no.nav.sokos

import no.nav.sokos.metrics.Metrics
import kotlin.properties.Delegates

class ApplicationState {
    var ready: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (!newValue) Metrics.appStateReadyFalse.inc()
    }
    var running: Boolean by Delegates.observable(true) { _, _, newValue ->
        if (!newValue) Metrics.appStateRunningFalse.inc()
    }
}
