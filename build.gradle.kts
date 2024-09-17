plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.epages.restdocs-api-spec") version "0.18.2"
}

group = "kgp.liivm"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}


val asciidoctorExt = configurations.create("asciidoctorExt") {
    extendsFrom(configurations["testImplementation"])
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.rest-assured:rest-assured:5.5.0")
    testImplementation("io.rest-assured:kotlin-extensions:5.5.0")
    implementation("com.google.guava:guava:33.3.0-jre")

    testImplementation("org.springframework.restdocs:spring-restdocs-restassured:3.0.1")
    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.1")
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor:3.0.1")

    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.18.2")
    testImplementation("com.epages:restdocs-api-spec-restassured:0.18.2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    val snippetsDir = file("build/generated-snippets")

    test {
        outputs.dir(snippetsDir)
    }

    asciidoctor {
        dependsOn(test)

        inputs.dir(snippetsDir)
        configurations(asciidoctorExt.name)
        baseDirFollowsSourceFile()
    }

    task<Copy>("generateDocumentation") {
        dependsOn(asciidoctor)

        delete(File("src/main/resources/static/docs"))

        copy {
            from("${asciidoctor.get().outputDir}")
            into("src/main/resources/static/docs")
        }
    }
}

postman {
    title = "My API"
    version = "0.1.0"
    baseUrl = "http://localhost:8080"
    snippetsDirectory = "build/generated-snippets"
}

openapi3 {
    this.setServer("http://localhost:8080")
    title = "My API"
    description = "My API description"
    version = "0.1.0"
    format = "json" // or json
}

