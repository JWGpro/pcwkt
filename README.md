# pcwkt

A work-in-progress Advance Wars clone written in Kotlin with libGDX/KTX, for PC and Android.

The primary focus of this project is to enable user-defined gamemodes and customisation (mods) to
create turn-based games which share common logic.
To this end, gamemode logic is defined in separate
Kotlin [plugins](https://github.com/JWGpro/pcwkt/tree/master/plugins), which can be loaded from
the [core](https://github.com/JWGpro/pcwkt/blob/master/core/src/com/pcwkt/game/ingame/InGameScreen.kt)
via a
PF4J-based [api](https://github.com/JWGpro/pcwkt/blob/master/api/src/main/java/com/example/api/GameMode.kt).
This approach has been POCed on Android, but is *not yet* fully supported there.
It contrasts with the deprecated
implementation [pcw_libGDX](https://github.com/JWGpro/pcw_libGDX), which defined gamemode logic in
Lua/Teal scripts.

Some features implemented:

- Attempts at design patterns; [Command](https://gameprogrammingpatterns.com/command.html) for
  "replays" (the ability to play back and rewind played
  games), [State](https://gameprogrammingpatterns.com/state.html).
- [A* pathfinding](https://en.wikipedia.org/wiki/A*_search_algorithm) written from scratch for
  evaluating valid routes
- Input mapping, multiplexed event handling
- A primitive map maker which can serialise to files
- TODO: Networking

The standard game
mode [Classic](https://github.com/JWGpro/pcwkt/tree/master/plugins/classic/src/main/java/com/example/classic)
(plugin
wrapper [here](https://github.com/JWGpro/pcwkt/blob/master/plugins/classic/src/main/java/com/example/classic/ClassicModePlugin.kt))
is intended to be mostly a straight reimplementation (clone) of core Advance
Wars gameplay, excluding COs, and with some alterations. A fog-of-war mode is planned as a separate
gamemode with considerable changes to mechanics and available units. Hexagonal grids are also
considered, either as an option to existing gamemodes, or as separate gamemodes.

Assets (maps, SFX, UI skins, unit and terrain sprites etc.) are loaded at runtime from an
[external directory](https://libgdx.com/wiki/file-handling) `pcwkt`, making them user-definable.
None of these assets have been included in this repository yet.

The entry point on desktop
is [here](https://github.com/JWGpro/pcwkt/blob/master/desktop/src/com/pcwkt/game/DesktopLauncher.kt).

# Importing and Running

Standard libGDX instructions apply: https://libgdx.com/dev/import-and-running/

If you have issues with Gradle sync or `./gradlew build`, ensure your Android SDK has installed
build tools 32.0.0.

You'll probably also need an Android debug keystore; move this to `~/.android`:

`$ keytool -genkey -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass
android -keyalg RSA -keysize 2048 -validity 10000`

Since gamemode logic exists as a plugin, that has to be built before running the application,
which will pick it up from the build directory.

Therefore set up your run configuration to run a Gradle build before `desktop:run` (or the Android
equivalent).

# License

Licensed under the GNU Affero General Public License 3 or any later version at your choice.

SPDX-License-Identifier: AGPL-3.0-or-later