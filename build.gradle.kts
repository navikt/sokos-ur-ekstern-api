group = "no.nav.sokos"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

val kotlinVersion = "1.9.23"
val ktorVersion = "2.3.10"
val jacksonVersion = "2.17.1"
val prometheusVersion = "1.12.5"
val logbackVersion = "1.5.6"
val logstashVersion = "7.4"
val swaggerUiVersion = "4.18.2"
val kotlinLoggingVersion = "3.0.5"
val commonsCodecVersion = "1.17.0"
val nimbusVersion = "9.37.3"
val junitVersion = "5.10.2"
val restAssuredVersion = "5.4.0"
val swaggerRequestValidatorVersion = "2.40.0"

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
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
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")


    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVersion")
    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")
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
