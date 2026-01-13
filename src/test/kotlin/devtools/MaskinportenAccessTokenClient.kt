package devtools

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.nimbusds.jose.jwk.RSAKey
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import no.nav.sokos.defaultHttpClient
import java.util.Date

suspend fun main() {
    val config = MaskinportenClientConfig()
    val maskinportenClient = MaskinportenAccessTokenClient(config, defaultHttpClient)
    val token = maskinportenClient.hentAccessTokenFraProvider()
    println(token.accessToken)
}

class MaskinportenAccessTokenClient(
    private val maskinportenConfig: MaskinportenClientConfig,
    private val client: HttpClient,
) {

    suspend fun hentAccessTokenFraProvider(): AccessToken {
        val jwt = JWT.create()
            .withAudience(maskinportenConfig.openIdConfiguration.issuer)
            .withIssuer(maskinportenConfig.clientId)
            .withClaim("scope", maskinportenConfig.scopes)
            .withExpiresAt(Date(System.currentTimeMillis() + 120000))
            .withIssuedAt(Date())
            .withKeyId( maskinportenConfig.rsaKey?.keyID)
            .sign(Algorithm.RSA256(null, maskinportenConfig.rsaKey?.toRSAPrivateKey()))

        return client.post(maskinportenConfig.openIdConfiguration.tokenEndpoint) {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.FormUrlEncoded)
            method = HttpMethod.Post
            setBody("grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$jwt")
        }.body()
    }
}


data class MaskinportenClientConfig(
    val clientId: String = readProperty("MASKINPORTEN_CLIENT_ID", ""),
    val authorityEndpoint: String = readProperty("MASKINPORTEN_WELL_KNOWN_URL", ""),
    val rsaKey: RSAKey? = RSAKey.parse(readProperty("MASKINPORTEN_CLIENT_JWK", "")),
    val scopes: String = readProperty("MASKINPORTEN_SCOPES", ""),
) : JwtConfig(authorityEndpoint)

data class OpenIdConfiguration(
    @param:JsonProperty("jwks_uri") val jwksUri: String,
    @param:JsonProperty("issuer") val issuer: String,
    @param:JsonProperty("token_endpoint") val tokenEndpoint: String,
)

open class JwtConfig(wellKnownUrl: String) {
    val openIdConfiguration: OpenIdConfiguration by lazy {
        runBlocking { defaultHttpClient.get(wellKnownUrl).body() }
    }
}

data class AccessToken(
    @param:JsonAlias("access_token")
    val accessToken: String
)


private fun readProperty(name: String, default: String? = null) =
    System.getenv(name)
        ?: System.getProperty(name)
        ?: default.takeIf { it != null }
        ?: throw RuntimeException("Mandatory property '$name' was not found")
