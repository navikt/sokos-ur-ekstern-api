package no.nav.sokos.metrics

import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry


object Metrics {
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        .apply { config().commonTags("team", "okonomi", "app", "sokos-ur-ekstern-api")}

    val appStateRunningFalse: io.micrometer.core.instrument.Counter = io.micrometer.core.instrument.Counter.builder("app.state.running.false")
        .description("App state running changed to false.")
        .register(prometheusRegistry)

    val appStateReadyFalse: io.micrometer.core.instrument.Counter = io.micrometer.core.instrument.Counter.builder("app.state.ready.false")
        .description("App state ready changed to false.")
        .register(prometheusRegistry)


}