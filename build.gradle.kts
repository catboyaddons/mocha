plugins {
	id("coffee.axle.blahaj")
}

blahaj {
	config {
		// yarn()
		// versionedAccessWideners()
	}
	setup {
		// mocha("1.0.0")
		sodium()
		iris()
		devauth()
		mixinExtras()
		hypixel()
/*
		txnilib("1.0.23")
		forgeConfig()
		conditionalMixin()

		// access Gradle's DependencyHandler

		// configure Curseforge & Modrinth publish settings
		incompatibleWith("optifine")

		// add mods with Blahaj's fluent interface

		addMod("sodiumextras")
			.modrinth("sodium-extras") // override with Modrinth URL slug
			.addPlatform("1.21.1-neoforge", "neoforge-1.21.1-1.0.7")
			.addPlatform("1.21.1-fabric", "fabric-1.21.1-1.0.7") { required() } */
	}
}
