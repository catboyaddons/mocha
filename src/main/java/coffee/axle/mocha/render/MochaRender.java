// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.render;

import coffee.axle.mocha.compat.RenderCompat;

/**
 * Public render abstraction surface for Mocha consumers (e.g. Latte).
 *
 * <p>All methods delegate to {@link RenderCompat} which contains
 * the version-specific implementation details. This class has no
 * Stonecutter guards and no direct MC imports.
 */
public final class MochaRender {

    private MochaRender() {}

    /**
     * Returns the width of the MC window's framebuffer in physical pixels.
     */
    public static int getFramebufferWidth() {
        return RenderCompat.getFramebufferWidth();
    }

    /**
     * Returns the height of the MC window's framebuffer in physical pixels.
     */
    public static int getFramebufferHeight() {
        return RenderCompat.getFramebufferHeight();
    }

    /**
     * Returns the device pixel ratio (framebuffer pixels per logical pixel).
     * Pass this value to NanoVG's {@code devicePixelRatio} parameter.
     */
    public static float getContentScale() {
        return RenderCompat.getContentScale();
    }

    /**
     * Composites a GL texture on top of the MC framebuffer.
     * Must be called from within a HUD render callback on the render thread.
     *
     * @param texId GL texture ID of the source image (typically an FBO colour attachment)
     * @param x     left edge in framebuffer pixels
     * @param y     top  edge in framebuffer pixels
     * @param w     width  in framebuffer pixels
     * @param h     height in framebuffer pixels
     */
    public static void compositeTexture(int texId, int x, int y, int w, int h) {
        RenderCompat.compositeTexture(texId, x, y, w, h);
    }

    /**
     * Registers a callback to be invoked during the HUD rendering phase,
     * after MC's own HUD elements have been drawn.
     *
     * @param callback called once per frame on the render thread
     */
    public static void onHudRender(Runnable callback) {
        RenderCompat.registerHudRender(callback);
    }
}
