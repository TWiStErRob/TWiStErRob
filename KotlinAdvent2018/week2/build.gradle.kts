dependencies {
	implementation(project("twinject"))
	implementation(project("twinject-reflect"))
}

tasks.register<JavaExec>("runGlobal") {
	mainClass = "net.twisterrob.challenges.adventOfKotlin2018.week2.global.CoffeeShopKt"
	classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register<JavaExec>("runInstantiate") {
	mainClass = "net.twisterrob.challenges.adventOfKotlin2018.week2.instantiate.CoffeeShopKt"
	classpath = sourceSets.main.get().runtimeClasspath
}

tasks.register<JavaExec>("runNoReflect") {
	mainClass = "net.twisterrob.challenges.adventOfKotlin2018.week2.noreflect.CoffeeShopKt"
	classpath = sourceSets.main.get().runtimeClasspath
}
