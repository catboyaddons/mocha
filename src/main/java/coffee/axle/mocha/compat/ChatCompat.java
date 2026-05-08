// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;


public final class ChatCompat {
// https://codeberg.org/stonecutter/docs/raw/branch/main/docs/wiki/start/comments.md
    private ChatCompat() {}
    //? if <26 {
    public static String platformName() {
        return "1.21.x";
    }
    //?} else {
    /*public static String platformName() {
        return "26x!!";
    }*///?}
}
