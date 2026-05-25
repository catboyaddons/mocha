// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.render;

import coffee.axle.mocha.compat.RenderCompat;

public final class MochaRender {

    private MochaRender() {}


    public static int getFramebufferWidth() {
        return RenderCompat.getFramebufferWidth();
    }

    public static int getFramebufferHeight() {
        return RenderCompat.getFramebufferHeight();
    }

    public static float getContentScale() {
        return RenderCompat.getContentScale();
    }

    public static void compositeTexture(int texId, int x, int y, int w, int h) {
        RenderCompat.compositeTexture(texId, x, y, w, h);
    }

    public static void onHudRender(Runnable callback) {
        RenderCompat.registerHudRender(callback);
    }
}
