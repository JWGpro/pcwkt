val appName: String by rootProject.extra

// TODO: Script is unfinished due to iOS not being a target

plugins {
    kotlin("jvm")
}

//apply(plugin = "robovm")
//
//dependencies {
//    val roboVMVersion: String by rootProject.extra
//    val gdxVersion: String by rootProject.extra
//    val gdxControllersVersion: String by rootProject.extra
//
//    implementation(project(":core"))
//    api("com.mobidevelop.robovm:robovm-rt:$roboVMVersion")
//    api("com.mobidevelop.robovm:robovm-cocoatouch:$roboVMVersion")
//    api("com.badlogicgames.gdx:gdx-backend-robovm:$gdxVersion")
//    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-ios")
//    api("com.badlogicgames.gdx-controllers:gdx-controllers-ios:$gdxControllersVersion")
//    api("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-ios")
//}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = "1.7"
    options.encoding = "UTF-8"
}

//sourceSets {
//    main {
//        java {
//            srcDirs("src/")
//        }
//    }
//}
//
//ext {
//    mainClassName = "com.pcwkt.game.IOSLauncher"
//}

//launchIPhoneSimulator.dependsOn build
//launchIPadSimulator.dependsOn build
//launchIOSDevice.dependsOn build
//createIPA.dependsOn build
//
//robovm {
//	archs = "thumbv7:arm64"
//}

eclipse {
    project {
        name = "${appName}-ios"
    }
}