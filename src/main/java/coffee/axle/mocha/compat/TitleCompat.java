// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class TitleCompat {
    private TitleCompat() {}

    public static void showTitle(Minecraft mc, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
        //? if <26.2 {
        mc.gui.setTitle(title);
        mc.gui.setSubtitle(subtitle);
        mc.gui.setTimes(fadeIn, stay, fadeOut);
        //?} else {
        /*mc.gui.hud.setTitle(title);
        mc.gui.hud.setSubtitle(subtitle);
        mc.gui.hud.setTimes(fadeIn, stay, fadeOut);
        *///?}
    }
}
