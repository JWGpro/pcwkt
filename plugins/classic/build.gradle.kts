plugins {
    kotlin("kapt")
}

dependencies {
    val pf4jVersion: String by rootProject.extra

    compileOnly(project(":api"))
    compileOnly(kotlin("stdlib"))

    compileOnly("org.pf4j:pf4j:${pf4jVersion}")
    kapt("org.pf4j:pf4j:${pf4jVersion}")
}
