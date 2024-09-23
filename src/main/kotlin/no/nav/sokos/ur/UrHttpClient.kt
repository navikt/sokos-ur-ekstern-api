package no.nav.sokos.ur

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson.jackson
import no.nav.sokos.config.Configuration
import no.nav.sokos.customConfig
import org.apache.http.impl.conn.SystemDefaultRoutePlanner
import java.io.FileInputStream
import java.net.ProxySelector
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

fun urHttpClient(urConfig: Configuration.UrConfig) = HttpClient(Apache) {
    engine {
        sslContext = sslContext(urConfig)
        customizeClient {
            setRoutePlanner(SystemDefaultRoutePlanner(ProxySelector.getDefault()))
        }
    }
    install(ContentNegotiation) {
        jackson {
            customConfig()
        }
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 300000
        connectTimeoutMillis = 10000
        socketTimeoutMillis = 300000
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.INFO
    }
}

private fun sslContext(urConfig: Configuration.UrConfig): SSLContext {
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        .apply {
            val keyStoreFile = FileInputStream(urConfig.trustStore)
            val keyStorePassword = urConfig.trustStorePassword.toCharArray()
            load(keyStoreFile, keyStorePassword)
        }

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        .apply { init(keyStore) }

    return SSLContext.getInstance("TLS")
        .apply { init(null, trustManagerFactory.trustManagers, null) }
}
