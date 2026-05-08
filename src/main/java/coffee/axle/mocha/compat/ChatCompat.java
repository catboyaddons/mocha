// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;


public final class ChatCompat {

    private ChatCompat() {}

    //? if mc >= "26.1.2" {
    public static String platformName() {
        return "26.x";
    }
    //?} else {
    /*public static String platformName() {
        return "1.21.x";
    }*/
    //?}
}
