buildscript {
	repositories {
		mavenCentral()
		gradlePluginPortal()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
	}
}

plugins {
	java
	kotlin("jvm") version "1.6.10" apply false
}

object ver {
	const val kotlin = "1.6.10"
	const val hamcrest2 = "2.0.0.0"
	const val junitJupiter = "5.8.2"
}

subprojects {
	apply(plugin = "java-library")
	apply(plugin = "kotlin")

	repositories {
		mavenCentral()
	}

	dependencies { // Kotlin
		implementation("org.jetbrains.kotlin:kotlin-stdlib:${ver.kotlin}")
		implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${ver.kotlin}")
	}

	dependencies { // Test
		testImplementation("org.junit.jupiter:junit-jupiter-api:${ver.junitJupiter}")
		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${ver.junitJupiter}")
		testImplementation("org.hamcrest:java-hamcrest:${ver.hamcrest2}")
	}

	tasks.withType<Test> {
		useJUnitPlatform()
		testLogging.events(/*"passed",*/ "skipped", "failed")
	}

	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = freeCompilerArgs + listOf(
				"-Xopt-in=kotlin.RequiresOptIn"
			)
		}
	}
}
