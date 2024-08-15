plugins {
    kotlin("kapt")
    // Needs to be here and not just in the API project or Serializers won't be found at runtime
    kotlin("plugin.serialization")
}

dependencies {
    val pf4jVersion: String by rootProject.extra
    val ktxVersion: String by rootProject.extra

    compileOnly(project(":api"))
    compileOnly(kotlin("stdlib"))

    compileOnly("org.pf4j:pf4j:${pf4jVersion}")
    kapt("org.pf4j:pf4j:${pf4jVersion}")

    api("io.github.libktx:ktx-inject:$ktxVersion")
}
