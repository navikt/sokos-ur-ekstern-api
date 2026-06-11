group = "no.nav.sokos"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "2.4.0"
    id("com.gradleup.shadow") version "9.4.2"
}

repositories {
    mavenCentral()
}

val kotlinVersion = "2.4.0"
val ktorVersion = "3.5.0"
val jacksonVersion = "2.22.0"
val prometheusVersion = "1.16.5"
val logbackVersion = "1.5.34"
val logstashVersion = "9.0"
val kotlinLoggingVersion = "8.0.4"
val commonsCodecVersion = "1.22.0"
val nimbusVersion = "10.9.1"
val junitVersion = "6.1.0"
val restAssuredVersion = "6.0.0"
val swaggerRequestValidatorVersion = "2.46.1"

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation(platform("io.netty:netty-bom:4.2.15.Final"))
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-client-apache5:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheusVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusVersion")
    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVersion")

    testImplementation("io.ktor:ktor-client-mock:$ktorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("com.atlassian.oai:swagger-request-validator-restassured:$swaggerRequestValidatorVersion")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    shadowJar {
        enabled = true
        archiveFileName.set("app.jar")
        manifest {
            attributes["Main-Class"] = "no.nav.sokos.MainKt"
        }
    }
    test {
        useJUnitPlatform()
    }
}
