import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.hidetake.swagger.generator") version "2.19.2" apply true
}

group = "no.nav.sokos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.7"
val jacksonVersion = "2.16.1"
val prometheusVersion = "1.12.2"
val logbackVersion = "1.4.14"
val logstashVersion = "7.4"
val swaggerUiVersion= "4.18.2"
val kotlinLoggingVersion = "3.0.5"
val commonsCodecVersion = "1.15"
val nimbusVersion =  "9.37.3"
val junitVersion = "5.10.1"

dependencies {
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-call-id:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt:$ktorVersion")
    implementation("io.ktor:ktor-server-double-receive:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("commons-codec:commons-codec:$commonsCodecVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheusVersion")

    implementation("com.nimbusds:nimbus-jose-jwt:$nimbusVersion")

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    implementation("ch.qos.logback:logback-core:$logbackVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")

    swaggerUI("org.webjars:swagger-ui:$swaggerUiVersion")


    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.21")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
}

kotlin {
    jvmToolchain(21)
}

swaggerSources {
    create("sokos_ur_ekstern").apply {
        setInputFile(file("spec/ur-ekstern-api-v1-openapi-spec.yaml"))
        ui.outputDir = layout.buildDirectory.dir("resources/main/api/sokos_ur_ekstern").get().asFile
    }
}

tasks {

    withType<Test>().configureEach {
        useJUnitPlatform()
        dependsOn(generateSwaggerUI)
    }
    withType<Jar>().configureEach {
        dependsOn(generateSwaggerUI)
    }

    withType<ShadowJar>().configureEach {
        enabled = true
        archiveFileName.set("app.jar")
        manifest {
            attributes["Main-Class"] = "no.nav.sokos.MainKt"
        }
        dependsOn(generateSwaggerUI)
    }
}
