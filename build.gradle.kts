plugins {
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.spring") version "2.2.0"
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "au.com.supernovae"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

repositories {
	mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.10.2")

	// Dev-dependencies
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Spring boot
	implementation("org.springframework.cloud:spring-cloud-starter-gateway-server-webflux:4.3.0")
	implementation("org.springframework.boot:spring-boot-configuration-metadata:3.5.4")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// Database
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.postgresql:r2dbc-postgresql")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	jvmToolchain(24)
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.register<Exec>("buildFrontend") {
	workingDir = file("./svelte")
	commandLine("bun", "run", "build")
}

tasks.register<Copy>("copyFrontend") {
	dependsOn("buildFrontend")
	mustRunAfter("processResources")
	from("./svelte/build") // Svelte build output
	into("src/main/resources/static/svelte")
}

// Full build task: builds backend + frontend
tasks.register("fullBuild") {
	dependsOn("build", "copyFrontend")
	group = "build"
}

tasks.register("fullBuildJar") {
	group = "build"
	description = "Builds frontend, then packages backend into executable JAR with prod profile."
	dependsOn("build", "copyFrontend", "bootJar")
}
