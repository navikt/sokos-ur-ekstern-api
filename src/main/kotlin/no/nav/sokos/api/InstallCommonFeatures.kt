package no.nav.sokos.api

import com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.annotation.JsonInclude.Value.construct
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
import io.ktor.server.plugins.calllogging.CallLogging
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
import no.nav.sokos.api.entitet.FinnYtelserForOrgnummerRequest
import no.nav.sokos.metrics.Metrics
import org.slf4j.event.Level
import java.util.UUID


fun Application.installCommonFeatures() {
    install(CallId) {
        header("x-correlation-id")
        generate { UUID.randomUUID().toString() }
        verify { it.isNotEmpty() }
    }
    install(CallLogging) {
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
            setDefaultPropertyInclusion(construct(NON_NULL, ALWAYS))
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
            val orgnr = when {
                call.request.path().endsWith("finn-ytelser-for-orgnummer") ->
                    runBlocking { call.receive<FinnYtelserForOrgnummerRequest>().orgnummer }

                call.request.path().endsWith("finn-ytelser") -> call.hentHjemmelshaver() ?: "MANGLER"

                else -> "n/a"
            }
            tag("orgnr", orgnr)
            tag("konsument", call.hentKallendeSystem() ?: "EKSTERN")
        }

    }
}

