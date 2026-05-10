import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.compile.JavaCompile

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

// Mocha targets Java 21 bytecode across all MC versions so downstream projects
// resolving at Java 21 (e.g. SigmaClient) can consume it without JVM compat errors.
// Mocha itself does not use any Java 22+ language features.
tasks.withType<JavaCompile>().configureEach {
	options.release.set(21)
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
		mavenLocal()
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
