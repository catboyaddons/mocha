# Mocha

Mocha is a shared Minecraft abstraction library for my (Axle) projects. It builds three JARs from a single source tree,, one per Minecraft version,,, and publishes them to [maven.axle.coffee/releases](https://maven.axle.coffee/releases). Downstream projects consume the correct JAR automatically through [blahaj](https://github.com/axlecoffee/blahaj), with no version-specific code on their end.

## Why It Exists

Minecraft's API surface shifts between minor versions. Without a shared layer, every project touching MC APIs -- SigmaClient, CatboyAddons, Latte and any of my other projects -- would each scatter inline `//? if mc >= "26.1"` Stonecutter guards through their own source trees. Mocha centralizes that problem once; and I get a clean, version-agnostic interface.

## Versions

Stonecutter compiles Mocha as follows:

| Subproject | Published Artifact |
|---|---|
| `1.21.10-fabric` | `mocha-<version>+1.21.10.jar` |
| `1.21.11-fabric` | `mocha-<version>+1.21.11.jar` |
| `26.1.2-fabric` | `mocha-<version>+26.1.2.jar` |

All artifacts are published to `maven.axle.coffee/releases` under the group `coffee.axle.mocha`.

## Using Mocha

Add the blahaj plugin to your project, then call `mocha()` in the setup block. blahaj resolves the correct MC-version-flavoured JAR automatically:

```kotlin
blahaj {
    setup {
        mocha("1.0.0")
    }
}
```

That's it. Import from `coffee.axle.mocha` directly no version guards required on your end.

### Repository

Declare the maven repository in `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        maven("https://maven.axle.coffee/releases")
    }
}
```

## Package Structure

```
coffee.axle.mocha/
├── compat/          # Stonecutter-guarded MC API adapters
│   └── ChatCompat.java # example
│ 
├── util/            # utils
│   ├── ChatUtil.java
│   ├── ColorUtil.java
│   └── TickScheduler.java
└── client/
    └── render/
        └── RenderHooks.java   # all render utils for Latte/anyhthing else
```

## Building

Requires Java 25+.

```sh
./gradlew buildAll
```

Publishing requires `MAVEN_USERNAME` and `MAVEN_SECRET` set as environment variables or Gradle properties.


## License

LGPL-3.0-only. See [LICENSES/LGPL-3.0-only.txt](LICENSES/LGPL-3.0-only.txt).

we are REUSE compliant! See [REUSE.toml](REUSE.toml) for details.

