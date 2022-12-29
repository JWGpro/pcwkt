plugins {
    kotlin("jvm")
}

dependencies {
    val pf4jVersion: String by rootProject.extra
    val gdxVersion: String by rootProject.extra

    implementation(kotlin("stdlib"))
    compileOnly("org.pf4j:pf4j:${pf4jVersion}")
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
}
