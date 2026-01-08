import com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    java
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.11.0"
    id("com.diffplug.spotless") version "7.0.4" apply true
}

group = "cm.klg"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

val mapstructVersion = "1.6.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")

    // POSTGRES
    implementation("org.postgresql:postgresql")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Mapstruct
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    annotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:$mapstructVersion")

    // OPENAPI
    implementation("io.swagger:swagger-annotations:1.6.11")
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<GenerateTask>("mainOpenApiGenerate") {
    generatorName = "spring"
    templateDir.set(file("$rootDir/openapi/templates/spring-boot").absolutePath)
    inputSpec.set(file("$rootDir/openapi/main.yaml").absolutePath)
    outputDir =
        layout.buildDirectory
            .dir("generated/sources/openapi")
            .get()
            .asFile.path
    apiPackage = "cm.klg.generated.monero.api"
    modelPackage = "cm.klg.generated.monero.dto"
    configOptions =
        mapOf(
            "dateLibrary" to "java8-localdatetime",
            "library" to "spring-boot",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "skipDefaultInterface" to "true",
            "useSpringBoot3" to "true",
        )
    typeMappings =
        mapOf(
            "time" to "java.time.LocalTime",
        )
    val generatedSourceCodeDir = file(outputDir.get() + "/src/main/java/cm/klg/generated/monero")
    doFirst {
        generatedSourceCodeDir.deleteRecursively()
    }
    onlyIf {
        !generatedSourceCodeDir.exists() ||
            file(inputSpec.get()).lastModified() > generatedSourceCodeDir.lastModified()
    }
}

tasks.compileJava.get().dependsOn(
    tasks["mainOpenApiGenerate"],
)

sourceSets.main
    .get()
    .java
    .srcDir(
        layout.buildDirectory
            .dir("generated/sources/openapi/src/main/java")
            .get()
            .asFile.path,
    )

spotless {
    java {
        targetExclude("build/**")
        toggleOffOn()
        googleJavaFormat("1.25.2")
            .reflowLongStrings()
            .formatJavadoc(true)
            .reorderImports(true)
            .groupArtifact("com.google.googlejavaformat:google-java-format")
    }
    kotlin {
        targetExclude("build/**")
        target("**/*.kts")
        ktlint("1.5.0")
    }
    format("xml", {
        targetExclude("build/**")
        target("src/**/*.xml")
        eclipseWtp(EclipseWtpFormatterStep.XML)
    })

    yaml {
        targetExclude("build/**")
        target("src/*/resources/**/*.yaml", "src/*/resources/**/*.yml", "specs/openapi/main.yaml")
        targetExclude("src/test/resources/docker-compose.yml")
        jackson()
            .feature("ORDER_MAP_ENTRIES_BY_KEYS", true)
    }
    gherkin {
        targetExclude("build/**")
        target("src/test/resources/**/*.feature")
        gherkinUtils()
            .version("9.0.0")
    }
}
