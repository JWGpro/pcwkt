package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

private interface Loadable {
    fun load(assetManager: AssetManager)
}

private const val EXT = "pcwkt"

enum class Assets {
    ;

    enum class Skins(val path: String) : Loadable {
        DEFAULT("$EXT/skins/glassy/skin/glassy-ui.json");

        override fun load(assetManager: AssetManager) {
            assetManager.load(path, Skin::class.java)
        }
    }

    enum class Textures(val path: String) : Loadable {
        INF_RED_1("$EXT/unit-assets/default/inf_red_1.png"),
        INF_RED_2("$EXT/unit-assets/default/inf_red_2.png"),
        INF_BLUE_1("$EXT/unit-assets/default/inf_blue_1.png"),
        INF_BLUE_2("$EXT/unit-assets/default/inf_blue_2.png"),
        APC_RED("$EXT/unit-assets/default/apc_red.png");

        override fun load(assetManager: AssetManager) {
            assetManager.load(path, Texture::class.java)
        }
    }

    enum class TextureAtlases(val path: String) : Loadable {
        ANIMS("$EXT/ui-assets/default/anims/anims.atlas");

        override fun load(assetManager: AssetManager) {
            assetManager.load(path, TextureAtlas::class.java)
        }
    }

    enum class Sounds(val path: String) : Loadable {
        FIRE("$EXT/sfx/fire.ogg"),
        DAMAGE("$EXT/sfx/damage.ogg");

        override fun load(assetManager: AssetManager) {
            assetManager.load(path, Sound::class.java)
        }
    }

    companion object {
        fun loadAll(assetManager: AssetManager) {
            Skins.values().forEach { x -> x.load(assetManager) }
            Textures.values().forEach { x -> x.load(assetManager) }
            TextureAtlases.values().forEach { x -> x.load(assetManager) }
            Sounds.values().forEach { x -> x.load(assetManager) }

            assetManager.finishLoading()
        }
    }
}
