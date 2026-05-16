plugins {
    id("com.gradleup.shadow")
}

version = "1.0.0"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    implementation(project(":common"))
}

tasks.shadowJar {
    archiveBaseName.set("LagoonPlugin-bukkit")
    archiveClassifier.set("")
    archiveVersion.set(version.toString())
}

tasks.build {
    dependsOn(tasks.shadowJar)
}