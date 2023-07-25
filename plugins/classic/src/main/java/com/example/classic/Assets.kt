package com.example.classic

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin

private interface Loadable {
    fun load(assetManager: AssetManager)
}

enum class Assets {
    ;

    enum class Skins(val path: String) : Loadable {
        DEFAULT("skins/glassy/skin/glassy-ui.json");

        override fun load(assetManager: AssetManager) {
            assetManager.load(this.path, Skin::class.java)
        }
    }

    enum class Textures(val path: String) : Loadable {
        INF_RED_1("unit-assets/default/inf_red_1.png"),
        INF_RED_2("unit-assets/default/inf_red_2.png"),
        INF_BLUE_1("unit-assets/default/inf_blue_1.png"),
        INF_BLUE_2("unit-assets/default/inf_blue_2.png"),
        APC_RED("unit-assets/default/apc_red.png");

        override fun load(assetManager: AssetManager) {
            assetManager.load(this.path, Texture::class.java)
        }
    }

    enum class TextureAtlases(val path: String) : Loadable {
        ANIMS("ui-assets/default/anims/anims.atlas");

        override fun load(assetManager: AssetManager) {
            assetManager.load(this.path, TextureAtlas::class.java)
        }
    }

    enum class Sounds(val path: String) : Loadable {
        FIRE("sfx/fire.ogg"),
        DAMAGE("sfx/damage.ogg");

        override fun load(assetManager: AssetManager) {
            assetManager.load(this.path, Sound::class.java)
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
