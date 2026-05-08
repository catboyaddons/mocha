import org.gradle.api.publish.maven.MavenPublication

plugins {
	id("coffee.axle.blahaj")
}

blahaj {
	config {
		// yarn()
		// versionedAccessWideners()
	}
	setup {}
}

// Derive the MC version from the Stonecutter subproject name, e.g. "1.21.10-fabric" -> "1.21.10".
// This must match the coordinate blahaj resolves: coffee.axle.mocha:mocha-<mcVersion>:<version>
val mcVersion = project.name.substringBeforeLast('-')

publishing {
	publications.named<MavenPublication>("mavenJava") {
		groupId = "coffee.axle.mocha"
		artifactId = "mocha-$mcVersion"
	}
	repositories {
		maven {
			name = "axle-maven"
			url = uri(System.getenv("MAVEN_URL") ?: "https://maven.axle.coffee/releases")
			credentials {
				username = System.getenv("MAVEN_USERNAME") ?: findProperty("MAVEN_USERNAME")?.toString()
				password = System.getenv("MAVEN_SECRET") ?: findProperty("MAVEN_SECRET")?.toString()
			}
		}
	}
}
