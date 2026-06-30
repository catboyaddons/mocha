// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.render;

import coffee.axle.mocha.compat.RenderCompat;
import java.util.HashMap;
import java.util.Map;

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

    public static void fireHudRender() {
        RenderCompat.fireHudRender();
    }

    // --- Canvas system for non-HUD overlays (ClickGUI, selectors, etc.) ---

    @FunctionalInterface
    public interface CanvasRenderer {
        void render(float windowW, float windowH, Object ctx);
    }

    private static final Map<String, CanvasRenderer> canvases = new HashMap<>();
    private static String activeCanvasId = null;

    /**
     * Register a custom canvas renderer.
     * @param id unique canvas identifier
     * @param renderer called every frame when this canvas is active
     */
    public static void registerCanvas(String id, CanvasRenderer renderer) {
        canvases.put(id, renderer);
    }

    /**
     * Unregister a canvas.
     */
    public static void unregisterCanvas(String id) {
        canvases.remove(id);
        if (id.equals(activeCanvasId)) {
            activeCanvasId = null;
        }
    }

    /**
     * Get the currently active canvas ID, or null if none.
     */
    public static String getActiveCanvas() {
        return activeCanvasId;
    }

    /**
     * Check if any canvas is currently active.
     */
    public static boolean hasActiveCanvas() {
        return activeCanvasId != null;
    }

    /**
     * Set the active canvas. Pass null to clear.
     */
    public static void setActiveCanvas(String id) {
        if (id != null && !canvases.containsKey(id)) {
            throw new IllegalArgumentException("Canvas not registered: " + id);
        }
        activeCanvasId = id;
    }

    /**
     * Fire the active canvas render. Called by LatteMcRenderer.
     * @param ctx the LatteContext, passed through to the renderer as an Object
     * @return true if a canvas was rendered, false if no active canvas.
     */
    public static boolean fireActiveCanvas(float windowW, float windowH, Object ctx) {
        if (activeCanvasId == null) return false;
        CanvasRenderer renderer = canvases.get(activeCanvasId);
        if (renderer != null) {
            renderer.render(windowW, windowH, ctx);
            return true;
        }
        return false;
    }
}

