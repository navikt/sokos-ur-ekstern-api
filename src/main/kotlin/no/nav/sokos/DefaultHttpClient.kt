package no.nav.sokos

import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.jackson.jackson
import java.net.ProxySelector
import org.apache.http.impl.conn.SystemDefaultRoutePlanner

fun ObjectMapper.customConfig() {
    registerModule(JavaTimeModule())
    configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}

val jsonMapper: ObjectMapper = jacksonObjectMapper().apply { customConfig() }

val defaultHttpClient = HttpClient(Apache) {
    install(ContentNegotiation){
        jackson {
            customConfig()
        }
    }
    install(HttpRequestRetry){
        retryOnException(maxRetries = 5)
        delayMillis { retry -> retry * 500L }
    }
    engine { customizeClient { setRoutePlanner(SystemDefaultRoutePlanner(ProxySelector.getDefault())) } }
}

