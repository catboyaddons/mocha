// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

//? if <26 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///?} else {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;
//?}
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Version-specific render utilities.
 * Window metrics use GLFW directly (stable across MC versions).
 * Composite blit uses a minimal GLSL program (no MC rendering pipeline).
 * HUD registration uses Fabric API events.
 */
public final class RenderCompat {

    private RenderCompat() {}

    // ── Window metrics ────────────────────────────────────────────────────────

    public static int getFramebufferWidth() {
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetFramebufferSize(GLFW.glfwGetCurrentContext(), w, h);
        return w[0];
    }

    public static int getFramebufferHeight() {
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetFramebufferSize(GLFW.glfwGetCurrentContext(), w, h);
        return h[0];
    }

    /**
     * Returns the device pixel ratio (framebuffer pixels / logical window pixels).
     * This is the value NanoVG expects for its {@code dpr} parameter.
     */
    public static float getContentScale() {
        long win = GLFW.glfwGetCurrentContext();
        int[] fbW = new int[1], winW = new int[1];
        GLFW.glfwGetFramebufferSize(win, fbW, null);
        GLFW.glfwGetWindowSize(win, winW, null);
        return winW[0] > 0 ? (float) fbW[0] / winW[0] : 1f;
    }

    // ── HUD render event ─────────────────────────────────────────────────────

    //? if <26 {
    /*public static void registerHudRender(Runnable callback) {
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> callback.run());
    }
    *///?} else {
    public static void registerHudRender(Runnable callback) {
        HudElementRegistry.addLast(
            Identifier.parse("mocha:latte-render"),
            (extractor, deltaTracker) -> callback.run()
        );
    }
    //?}

    // ── Full-screen composite blit ───────────────────────────────────────────

    // Lazy-initialised once per GL context lifetime.
    private static int blitProgram = 0;
    private static int blitVao    = 0;
    private static int blitVbo    = 0;

    /**
     * Blits {@code texId} as a full-screen alpha-blended overlay.
     * The texture is expected to be at framebuffer pixel resolution with
     * origin at GL bottom-left (standard FBO orientation).
     *
     * <p>Must be called on the render thread. Saves and restores the current
     * shader program, VAO binding, blend state, and depth-test state.
     *
     * @param texId GL texture ID (typically an FBO colour attachment)
     * @param x     left edge in framebuffer pixels (ignored in fullscreen blit)
     * @param y     top edge in framebuffer pixels  (ignored in fullscreen blit)
     * @param w     width  in framebuffer pixels    (ignored in fullscreen blit)
     * @param h     height in framebuffer pixels    (ignored in fullscreen blit)
     */
    public static void compositeTexture(int texId, int x, int y, int w, int h) {
        ensureBlit();

        // Save state that NanoVG (or MC) may have left dirty.
        int prevProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int prevVao     = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        boolean wasBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean wasDepth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);

        // Draw alpha-blended fullscreen quad, NDC space — no projection needed.
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL20.glUseProgram(blitProgram);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        GL20.glUniform1i(GL20.glGetUniformLocation(blitProgram, "tex"), 0);

        GL30.glBindVertexArray(blitVao);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(prevVao);

        // Restore state.
        GL20.glUseProgram(prevProgram);
        if (!wasBlend) GL11.glDisable(GL11.GL_BLEND);
        if (wasDepth)  GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private static void ensureBlit() {
        if (blitProgram != 0) return;

        // Minimal GLSL 330 core blit shader — no MC shader pipeline involved.
        String vsrc =
            "#version 330 core\n" +
            "layout(location=0) in vec2 pos;\n" +
            "layout(location=1) in vec2 uv;\n" +
            "out vec2 vUv;\n" +
            "void main() { vUv = uv; gl_Position = vec4(pos, 0.0, 1.0); }";

        String fsrc =
            "#version 330 core\n" +
            "in vec2 vUv;\n" +
            "uniform sampler2D tex;\n" +
            "out vec4 col;\n" +
            "void main() { col = texture(tex, vUv); }";

        int vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vs, vsrc);
        GL20.glCompileShader(vs);

        int fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fs, fsrc);
        GL20.glCompileShader(fs);

        blitProgram = GL20.glCreateProgram();
        GL20.glAttachShader(blitProgram, vs);
        GL20.glAttachShader(blitProgram, fs);
        GL20.glLinkProgram(blitProgram);
        GL20.glDeleteShader(vs);
        GL20.glDeleteShader(fs);

        // Fullscreen quad in NDC. UV is y-flipped: GL tex V=1 is image top,
        // and screen NDC y=1 is also top, so V mirrors screen Y correctly.
        // Layout per vertex: [pos.x, pos.y, uv.u, uv.v]
        float[] verts = {
            -1f,  1f,  0f, 1f,   // top-left
             1f,  1f,  1f, 1f,   // top-right
            -1f, -1f,  0f, 0f,   // bottom-left
             1f, -1f,  1f, 0f,   // bottom-right
        };

        blitVao = GL30.glGenVertexArrays();
        blitVbo = GL15.glGenBuffers();

        GL30.glBindVertexArray(blitVao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, blitVbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verts, GL15.GL_STATIC_DRAW);

        int stride = 4 * Float.BYTES;
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(0, 2, GL11.GL_FLOAT, false, stride, 0L);
        GL20.glEnableVertexAttribArray(1);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, stride, 2L * Float.BYTES);

        GL30.glBindVertexArray(0);
    }
}
