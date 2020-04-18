buildscript {
	repositories {
		jcenter()
		gradlePluginPortal()
	}
	dependencies {
		classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
	}
}

plugins {
	java
	kotlin("jvm") version "1.3.72" apply false
}

object ver {
	const val kotlin = "1.3.72"
	const val hamcrest2 = "2.0.0.0"
	const val junitJupiter = "5.6.2"
}

subprojects {
	apply(plugin = "java-library")
	apply(plugin = "kotlin")

	repositories {
		jcenter()
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
			languageVersion = "1.4"
			freeCompilerArgs = freeCompilerArgs + listOf(
				"-Xopt-in=kotlin.RequiresOptIn"
			)
		}
	}
}
