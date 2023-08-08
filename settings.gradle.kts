include(listOf("desktop", "android", "ios", "core", "api", "plugins", "plugins:classic"))

// TODO: Why can't you just put `val kotlinVersion = "thing"` here?

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion // org.jetbrains.kotlin.
        kotlin("plugin.serialization") version kotlinVersion
    }
}

extra.apply {
    val kotlinVersion: String by settings

    set("kotlinVersion", kotlinVersion)
}