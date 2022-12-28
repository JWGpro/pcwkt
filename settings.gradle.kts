include(listOf("desktop", "android", "ios", "core"))

// TODO: Why can't you just put `val kotlinVersion = "thing"` here?

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion // org.jetbrains.kotlin.jvm
    }
}

extra.apply {
    val kotlinVersion: String by settings

    set("kotlinVersion", kotlinVersion)
}