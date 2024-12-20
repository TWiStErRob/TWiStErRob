plugins {
	alias(libs.plugins.kotlin) apply false
}

val libss = libs
subprojects {
	apply(plugin = "org.gradle.java-library")
	apply(plugin = "org.jetbrains.kotlin.jvm")

	dependencies { // Test
		"testImplementation"(libss.kotlin.test)
		"testImplementation"(libss.junit.jupiter)
		"testRuntimeOnly"(libss.junit.launcher)
		"testImplementation"(libss.hamcrest)
	}

	tasks.withType<Test>().configureEach {
		useJUnitPlatform()
		testLogging.events("passed", "skipped", "failed")
	}
}
