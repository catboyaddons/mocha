// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

public final class ChatCompat {
    private ChatCompat() {}

    public static void addMessage(net.minecraft.client.gui.components.ChatComponent chat, net.minecraft.network.chat.Component message) {
        //? if <26 {
        /*chat.addMessage(message);
        *///?} else {
        chat.addClientSystemMessage(message);
        //?}
    }
}
