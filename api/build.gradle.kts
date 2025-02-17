plugins {
    kotlin("jvm")
}

dependencies {
    val pf4jVersion: String by rootProject.extra
    val gdxVersion: String by rootProject.extra

    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    compileOnly("org.pf4j:pf4j:${pf4jVersion}")
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    // Needs to be here and not in a mode project or you get a runtime LinkageError
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()

    testLogging {     // This is for logging and can be removed.
        events("passed", "skipped", "failed")
    }
}
