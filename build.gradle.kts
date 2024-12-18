plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "2.0.21"
    kotlin("plugin.allopen") version "1.9.22"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.owasp.dependencycheck") version "11.1.0"
}

group = "com.ssd"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

ext {
    set("testcontainers.version", "1.19.8")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.21.0")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.owasp:dependency-check-gradle:10.0.3")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencyCheck {
    skipConfigurations = mutableListOf(
        "errorprone",
        "checkstyle",
        "annotationProcessor",
        "java9AnnotationProcessor",
        "moduleAnnotationProcessor",
        "testAnnotationProcessor",
        "testJpmsAnnotationProcessor",
        "animalsniffer",
        "spotless996155815", // spotless996155815 is a weird configuration that's only added in jaeger-proto, jaeger-remote-sampler
        "js2p",
        "jmhAnnotationProcessor",
        "jmhBasedTestAnnotationProcessor",
        "jmhCompileClasspath",
        "jmhRuntimeClasspath",
        "jmhRuntimeOnly"
    )
    nvd.apiKey = System.getenv("NVD_API_KEY")
    failOnError = true
    failBuildOnCVSS = 7.0f // fail on high or critical CVE

}