plugins {
    id("java-library")
    id("org.allaymc.gradle.plugin") version "0.2.1"
}

group = "com.atri0110.playerstatstracker"
description = "A comprehensive player statistics tracking plugin with leaderboards and achievements"
version = "0.1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allay {
    // Updated to latest API version 0.24.0
    api = "0.24.0"

    plugin {
        entrance = ".PlayerStatsTracker"
        authors += "atri-0110"
        website = "https://github.com/atri-0110/PlayerStatsTracker"
    }
}

dependencies {
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.34")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.34")
}
