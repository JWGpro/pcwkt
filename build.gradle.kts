plugins {
    idea
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

buildscript {
    val kotlinVersion: String by extra

    extra.apply {
        set("appName", "PCWKT")
        set("gdxVersion", "1.11.0")
        set("ktxVersion", "1.11.0-rc2")
        set("roboVMVersion", "2.3.16")
        set("box2DLightsVersion", "1.5")
        set("ashleyVersion", "1.7.4")
        set("aiVersion", "1.8.2")
        set("gdxControllersVersion", "2.2.1")
        set("pf4jVersion", "3.8.0")
        set("pluginsDir", file("$buildDir/plugins"))
    }

    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        google()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("com.mobidevelop.robovm:robovm-gradle-plugin:2.3.16")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

allprojects {
    apply(plugin = "eclipse")

    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/releases/")
        maven("https://jitpack.io")
    }
}
