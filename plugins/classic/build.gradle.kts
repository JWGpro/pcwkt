plugins {
    kotlin("kapt")
}

dependencies {
//    val pf4jVersion: String by rootProject.extra
    val pf4jDir: String by rootProject.extra

    compileOnly(project(":api"))
    compileOnly(kotlin("stdlib"))

    compileOnly(files(pf4jDir)) // TODO: here
    kapt(files(pf4jDir))
}
