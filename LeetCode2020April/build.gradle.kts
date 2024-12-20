plugins {
	id("org.jetbrains.kotlin.jvm") version "2.1.0" apply false
}

object ver {
	const val hamcrest2 = "2.0.0.0"
	const val junitJupiter = "5.11.4"
}

subprojects {
	apply(plugin = "org.gradle.java-library")
	apply(plugin = "org.jetbrains.kotlin.jvm")

	repositories {
		mavenCentral()
	}

	dependencies { // Test
		"testImplementation"("org.junit.jupiter:junit-jupiter:${ver.junitJupiter}")
		"testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
		"testImplementation"("org.hamcrest:java-hamcrest:${ver.hamcrest2}")
	}

	tasks.withType<Test>().configureEach {
		useJUnitPlatform()
		testLogging.events(/*"passed",*/ "skipped", "failed")
	}

	extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension>("kotlin") {
		compilerOptions {
			freeCompilerArgs.add(
				"-Xopt-in=kotlin.RequiresOptIn",
			)
		}
	}
}
