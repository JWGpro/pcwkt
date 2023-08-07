plugins {
    kotlin("kapt")
    // TODO: Version should be the same as kotlinVersion. Dunno how to do it gracefully right now.
    kotlin("plugin.serialization") version "1.7.20"
}

dependencies {
    val pf4jVersion: String by rootProject.extra

    compileOnly(project(":api"))
    compileOnly(kotlin("stdlib"))

    compileOnly("org.pf4j:pf4j:${pf4jVersion}")
    kapt("org.pf4j:pf4j:${pf4jVersion}")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}
