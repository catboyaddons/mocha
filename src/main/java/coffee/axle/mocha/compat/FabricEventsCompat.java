// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

import java.util.function.Consumer;
//? if <26 {
/*import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
*///?} else {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
//?}

public final class FabricEventsCompat {
    private FabricEventsCompat() {}

    /**
     * Registers a callback on the end-of-main-render event.
     * The callback receives the render context as {@code Object}
     * (WorldRenderContext on pre-26, LevelRenderContext on post-26).
     */
    public static void registerEndMainRender(Consumer<Object> callback) {
        //? if <26 {
        /*WorldRenderEvents.END_MAIN.register(ctx -> callback.accept(ctx));
        *///?} else {
        LevelRenderEvents.END_MAIN.register(ctx -> callback.accept(ctx));
        //?}
    }

    /**
     * Registers a callback fired after the client level/world changes.
     */
    public static void registerWorldChange(Runnable callback) {
        //? if <26 {
        /*ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register((client, world) -> callback.run());
        *///?} else {
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((client, world) -> callback.run());
        //?}
    }
}
