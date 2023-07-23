plugins {
    kotlin("jvm")
}

dependencies {
//    val pf4jVersion: String by rootProject.extra
    val pf4jDir: String by rootProject.extra
    val gdxVersion: String by rootProject.extra

    implementation(kotlin("stdlib"))
    compileOnly(files(pf4jDir)) // TODO: here
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
}
