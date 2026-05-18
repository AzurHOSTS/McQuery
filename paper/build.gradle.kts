plugins {
    id("com.gradleup.shadow")
    id("xyz.jpenilla.run-paper")
}

version = "1.0.0"

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation(project(":common"))
    implementation(project(":bukkit"))
}
tasks.shadowJar {
    archiveBaseName.set("McQuery-paper")
    archiveClassifier.set("")
    archiveVersion.set(version.toString())
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
// Only for dev
tasks.runServer {
    minecraftVersion("1.21.11")
    runDirectory.set(file("run"))
}
