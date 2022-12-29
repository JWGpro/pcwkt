val appName: String by rootProject.extra

plugins {
    kotlin("jvm")
}

dependencies {
    val gdxVersion: String by rootProject.extra
    val gdxControllersVersion: String by rootProject.extra
    val ktxVersion: String by rootProject.extra
    val pf4jVersion: String by rootProject.extra

    implementation(project(":api"))
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("com.badlogicgames.gdx-controllers:gdx-controllers-core:$gdxControllersVersion")
    api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    api("io.github.libktx:ktx-app:$ktxVersion")
    implementation ("org.pf4j:pf4j:${pf4jVersion}")
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "1.7"
    options.encoding = "UTF-8"
}

sourceSets {
    main {
        java {
            srcDirs("src/")
        }
    }
}

eclipse {
    project {
        name = "${appName}-core"
    }
}

