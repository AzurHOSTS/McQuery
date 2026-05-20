plugins {
    java
    id("com.gradleup.shadow") version "8.3.6" apply false
    id("xyz.jpenilla.run-paper") version "2.3.1" apply false
}

group = "com.azurhosts"
version = "1.0"

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/groups/public/")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}