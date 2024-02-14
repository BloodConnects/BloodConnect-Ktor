import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension

plugins {
    kotlin("jvm") version "1.9.22"
    id("io.ktor.plugin") version "2.3.7"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.cloud.tools.appengine") version "2.4.2"
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
    }
}

group = "com.raktacare"
version = "0.0.1"

application {
    mainClass.set("com.raktacare.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-openapi")
    implementation("io.ktor:ktor-server-call-logging-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("org.postgresql:postgresql:42.5.1")
    implementation("io.ktor:ktor-server-websockets-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    implementation("org.jetbrains.exposed:exposed-core:0.44.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.44.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.22")
    testImplementation("io.ktor:ktor-server-test-host-jvm:2.3.7")
}

tasks {
    create("stage").dependsOn("installDist")
}
tasks.withType<Jar> {
    destinationDirectory = File("$rootDir/output")
}

configure<AppEngineAppYamlExtension> {
    stage {
        setArtifact("output/${project.name}-all.jar")
    }
    deploy {
        version = "GCLOUD_CONFIG"
        projectId = "GCLOUD_CONFIG"
    }
}