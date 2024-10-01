package no.nav.sokos.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.ktor.server.request.path
import io.ktor.server.request.receive
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import no.nav.sokos.api.entitet.FinnYtelserRequest
import no.nav.sokos.metrics.Metrics
import no.nav.sokos.secureLogger
import org.slf4j.event.Level
import java.util.UUID


fun Application.installCommonFeatures() {
    install(CallId) {
        header("nav-call-id")
        generate { UUID.randomUUID().toString() }
        verify { it.isNotEmpty() }
    }
    install(CallLogging) {
        logger = KotlinLogging.logger {}
        level = Level.INFO
        callIdMdc("x-correlation-id")
        disableDefaultColors()
        filter { call ->
            !call.request.path().contains("/docs")
                    && !call.request.path().contains("/internal")
                    && !call.request.path().contains("/metrics")
        }
    }
    install(ContentNegotiation) {
        jackson {
            registerKotlinModule()
            registerModule(JavaTimeModule())
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    install(DoubleReceive)
    install(MicrometerMetrics) {
        registry = Metrics.prometheusRegistry
        meterBinders = listOf(
            UptimeMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            JvmThreadMetrics(),
            ProcessorMetrics()
        )
        timers { call, _ ->
            secureLogger.info { "Modifiserer ktor timer for path ${call.request.path()}" }
            if (call.request.path().startsWith("/ur-ekstern/api/v1/finn-ytelser")) {
                secureLogger.info { "I if statement" }
                call.hentHjemmelshaver()?.let {
                    secureLogger.info { "Vi har et orgnr $it" }
                    tag("orgnr", it) } ?: tag("orgnr", "n/a")
                runBlocking {
                    runCatching {
                        call.receive<FinnYtelserRequest>().ytelseskoder?.forEach {
                            secureLogger.info { "Vi har ytelsestype: $it" }
                            tag("ytelsestype", it)
                        }
                    }
                }

            }
            secureLogger.info { "All done." }
        }
    }
}
