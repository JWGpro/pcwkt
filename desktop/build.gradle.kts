import org.gradle.internal.os.OperatingSystem

val appName: String by rootProject.extra

plugins {
    kotlin("jvm")
}

dependencies {
    val gdxVersion: String by rootProject.extra
    val gdxControllersVersion: String by rootProject.extra

    implementation(project(":core"))
    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    api("com.badlogicgames.gdx-controllers:gdx-controllers-desktop:$gdxControllersVersion")
    api("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "1.8"
}

sourceSets {
    main {
        java {
            srcDirs("src/")
        }
        resources {
            srcDirs("../assets")
        }
    }
}

val mainClassName = "com.pcwkt.game.DesktopLauncher"
val assetsDir = File("../assets")

tasks.register<JavaExec>("run") {
    dependsOn(tasks["classes"])

    main = mainClassName
    classpath = java.sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true

    if (OperatingSystem.current() == OperatingSystem.MAC_OS) {
        // Required to run on macOS
        jvmArgs?.add("-XstartOnFirstThread")
    }
}

tasks.register<JavaExec>("debug") {
    dependsOn(tasks["classes"])

    main = mainClassName
    classpath = java.sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = assetsDir
    isIgnoreExitValue = true
    debug = true
}

// TODO: Not yet tested
tasks.register<Jar>("dist") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = mainClassName
    }
    dependsOn(configurations.runtimeClasspath)
    from (
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    )
    with(tasks.jar.get() as CopySpec)
}
tasks["dist"].dependsOn(tasks["classes"])

eclipse {
    project {
        name = "${appName}-desktop"
    }
}