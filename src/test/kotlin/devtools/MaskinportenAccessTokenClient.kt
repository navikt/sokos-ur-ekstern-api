package devtools

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.annotation.JsonAlias
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import no.nav.sokos.config.Configuration
import no.nav.sokos.defaultHttpClient
import java.util.Date

suspend fun main() {
    val config = Configuration.MaskinportenClientConfig()
    val maskinportenClient = MaskinportenAccessTokenClient(config, defaultHttpClient)
    val token = maskinportenClient.hentAccessTokenFraProvider()
    println(token.accessToken)
}

class MaskinportenAccessTokenClient(
    private val maskinportenConfig: Configuration.MaskinportenClientConfig,
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

data class AccessToken(
    @JsonAlias("access_token")
    val accessToken: String
)

