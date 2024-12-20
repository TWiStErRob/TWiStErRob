@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
	repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
	repositories {
		mavenCentral()
	}
}

include(":week1")
include(":week2")
include(":week2:twinject")
include(":week2:twinject-reflect")
include(":week3")
include(":week4")
