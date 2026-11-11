# Minecraft 26.2 and Skia migration

Toolbox is a client framework for registering tools as modules, grouping them into categories, displaying their controls, and persisting state, settings and favorites. The active interface is `Test/Screens/ModulesScreen`, with a sidebar, category list, paginated module cards and module settings screens. Other mods can implement the module/setting interfaces to supply tools. Normal clients do not register the development showcase modules.

## Rendering and resources

Toolbox now uses the Minecraft 26.2 / Skia build of Squid. Every render override receives `SkiaContext`; helpers use the `SkiaHelper` API. Toolbox no longer depends on NanoVG or its native library. Squid draws with Skia into reusable Minecraft image memory and submits the result through Blaze3D, supporting both OpenGL and Vulkan. Raster drawing and full-frame texture uploads remain the renderer's performance tradeoff. See [Fabric's rendering guidance](https://docs.fabricmc.net/develop/rendering/basic-concepts).

Bundled fonts, SVG icons and theme colors load through Minecraft resource reload listeners. Fonts use actual Skia holders, including Squid's shared default font holders, rather than matching string identifiers. Existing font/icon references remain valid across resource reloads; removed/replaced native resources and input streams are released. Native resources are also closed at shutdown. Reopen a screen to apply color overrides copied into widget styles.

The slider adapter explicitly maps progress from 0 to 1 to the setting's bounds, preserves 10% wheel increments, and handles fixed bounds. Fractional numeric settings retain their precision. Opening a settings screen preserves the value without queuing a save. Existing JSON configuration paths and format remain unchanged.

## Build and install

Use Java 25 and run `gradlew.bat build`. The wrapper uses Gradle 9.8.2, Loom 1.19.3, Fabric Loader 0.20.1 and Fabric API 0.161.0+26.2. Yarn mappings and the remapping step have been removed.

By default, Gradle builds the matching Squid checkout at `../../Squid/Squid` automatically. Override it with `-Psquid_project="D:/path/to/Squid"`, or build against an existing mod jar with `-Psquid_jar="D:/path/to/Squid-1.0.0-alpha.5.jar"`. The Squid artifact must be the migrated Minecraft 26.2 / Skia build; the older NanoVG artifact is incompatible.

Install `build/libs/Toolbox-2.0.0-alpha.4.jar` together with the matching Squid mod and Fabric API. Squid supplies the Skia bindings and native libraries. Toolbox keeps Squid as a separate required mod.

## Verification

`gradlew.bat build` passes 15 regression tests covering bundled fonts, font/SVG reload and cleanup, layered theme colors, configuration compatibility, numeric precision, slider normalization and input, and carousel rendering/navigation.

`gradlew.bat runUiTest` opens the real modules UI with development fixtures in the isolated `run/ui-test` directory. It tests module toggles, favorites, click actions, boolean controls and numeric sliders. The fixtures are enabled only in development with `toolbox.testScreen=true`; the normal key binding remains Right Shift.

Validated on Windows with OpenGL and Vulkan, including font/icon rendering, module and setting interactions, persisted state/favorites/settings across restart, resource reload, window resizing and GUI scale 3. Other operating systems have not been exercised in this migration.
