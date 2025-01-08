If you have issues with Gradle sync or `./gradlew build`, ensure your Android SDK has installed
build tools 32.0.0.

You'll probably also need an Android debug keystore thing:
`$ keytool -genkey -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass
android -keyalg RSA -keysize 2048 -validity 10000`
Move that to `~/.android`.

Since the gamemode logic exists as a plugin, that has to be built before running the application,
which will pick it up from the build directory.
Therefore set up your run configuration to run a Gradle build before `desktop:run` (or the Android
equivalent).

# License

Licensed under the GNU Affero General Public License 3 or any later version at your choice.

SPDX-License-Identifier: AGPL-3.0-or-later