import org.gradle.api.tasks.bundling.Jar
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	java
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
}

val projectGroup: String by project
val projectVersion: String by project
val springBootStarterVersion: String by project
val springKafkaVersion: String by project
val lombokVersion: String by project
val jacksonDatabindVersion: String by project

group = projectGroup
version = projectVersion

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter:$springBootStarterVersion")
	implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")
	implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonDatabindVersion")
	implementation("org.projectlombok:lombok:$lombokVersion")


	testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootStarterVersion")
	testImplementation("org.springframework.kafka:spring-kafka-test:$springKafkaVersion")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.getByName<BootJar>("bootJar") {
	enabled = false
}

tasks.getByName<Jar>("jar") {
	enabled = true
}

tasks.withType<Test> {
	useJUnitPlatform()
}
