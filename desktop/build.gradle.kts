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
val pluginsDir: File by rootProject.extra

tasks.register<JavaExec>("run") {
    dependsOn(tasks["classes"])

    // For development
    systemProperty("pf4j.pluginsDir", pluginsDir.absolutePath)

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

tasks.register<Jar>("uberJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(tasks.named("compileKotlin"))
    archiveClassifier.set("uber")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    manifest {
        attributes["Main-Class"] = mainClassName
    }

    archiveBaseName.set("${project.name}-plugin-demo")
}

tasks.named("build") {
    dependsOn("uberJar")
}

eclipse {
    project {
        name = "${appName}-desktop"
    }
}