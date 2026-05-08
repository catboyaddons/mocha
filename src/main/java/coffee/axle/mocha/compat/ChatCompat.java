// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

import net.minecraft.text.Text;


public final class ChatCompat {

    private ChatCompat() {}

    //? if mc >= "26.1.2" {
    public static Text literal(String text) {
        return Text.literal(text);
    }
    //?} else {
    /*public static Text literal(String text) {
        return Text.literal(text);
    }*/
    //?}
}
