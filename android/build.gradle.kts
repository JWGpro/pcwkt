import java.util.Properties

val appName: String by rootProject.extra

plugins {
    id("com.android.application")
    kotlin("android")
}

val natives: Configuration by configurations.creating

dependencies {
    val gdxVersion: String by rootProject.extra
    val gdxControllersVersion: String by rootProject.extra

    implementation(project(":core"))
    api("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
    api("com.badlogicgames.gdx-controllers:gdx-controllers-android:$gdxControllersVersion")
    api("com.badlogicgames.gdx:gdx-freetype:$gdxVersion")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-armeabi-v7a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-arm64-v8a")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86")
    natives("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-x86_64")
}

android {
    buildToolsVersion = "32.0.0"
    compileSdk = 31
    sourceSets {
        named("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            aidl.srcDirs("src")
            renderscript.srcDirs("src")
            res.srcDirs("res")
            assets.srcDirs("../assets")
            jniLibs.srcDirs("libs")
        }

    }
    packagingOptions {
        resources.excludes.add("META-INF/robovm/ios/robovm.xml")
    }
    defaultConfig {
        applicationId = "com.pcwkt.game"
        minSdk = 16
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}


// called every time gradle gets executed, takes the native dependencies of
// the natives configuration, and extracts them to the proper libs/ folders
// so they get packed with the APK.
tasks.register("copyAndroidNatives") {
    doFirst {
        file("libs/armeabi-v7a/").mkdirs()
        file("libs/arm64-v8a/").mkdirs()
        file("libs/x86_64/").mkdirs()
        file("libs/x86/").mkdirs()

        configurations.named("natives").get().copy().files.forEach { jar ->
            var outputDir: File? = null
            if (jar.name.endsWith("natives-arm64-v8a.jar")) outputDir = file("libs/arm64-v8a")
            if (jar.name.endsWith("natives-armeabi-v7a.jar")) outputDir = file("libs/armeabi-v7a")
            if (jar.name.endsWith("natives-x86_64.jar")) outputDir = file("libs/x86_64")
            if (jar.name.endsWith("natives-x86.jar")) outputDir = file("libs/x86")
            if (outputDir != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.whenTaskAdded {
    if (name.contains("merge") && name.contains("JniLibFolders")) {
        dependsOn("copyAndroidNatives")
    }
}

tasks.register<Exec>("run") {
    val path: String
    val localProperties = project.file("../local.properties")

    path = if (localProperties.exists()) {
        val properties = Properties()
        properties.load(localProperties.inputStream())

        properties.getProperty("sdk.dir", System.getenv("ANDROID_HOME"))
    } else {
        System.getenv("ANDROID_HOME")
    }

    val adb = "$path/platform-tools/adb"
    commandLine = listOf(adb, "shell", "am", "start", "-n", "com.pcwkt.game/com.pcwkt.game.AndroidLauncher")
}

eclipse {
    project {
        name = "${appName}-android"
    }
}
