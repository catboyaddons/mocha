// SPDX-FileCopyrightText: 2026 Axle Duggan (axlecoffee) <contact@axle.coffee>
//
// SPDX-License-Identifier: LGPL-3.0-only

package coffee.axle.mocha.compat;

//? if <26 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
*///?}
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


public final class RenderCompat {

    private RenderCompat() {}

    public static int getFramebufferWidth() {
        return Minecraft.getInstance().getWindow().getScreenWidth();
    }

    public static int getFramebufferHeight() {
        return Minecraft.getInstance().getWindow().getScreenHeight();
    }

    public static float getContentScale() {
        var window = Minecraft.getInstance().getWindow();
        int fbW = window.getScreenWidth();
        int winW = window.getWidth();
        return winW > 0 ? (float) fbW / winW : 1f;
    }


    private static Runnable hudRenderCallback = null;

    //? if <26 {
    /*public static void registerHudRender(Runnable callback) {
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> callback.run());
    }
    *///?} else {
    public static void registerHudRender(Runnable callback) {
        hudRenderCallback = callback;
    }
    //?}

    public static void fireHudRender() {
        if (hudRenderCallback != null) {
            hudRenderCallback.run();
        }
    }


    private static int blitProgram = 0;
    private static int blitVao    = 0;
    private static int blitVbo    = 0;


    public static void compositeTexture(int texId, int x, int y, int w, int h) {
        ensureBlit();

        int prevProgram = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
        int prevVao     = GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING);
        boolean wasBlend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean wasDepth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL20.glUseProgram(blitProgram);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
        GL20.glUniform1i(GL20.glGetUniformLocation(blitProgram, "tex"), 0);

        GL30.glBindVertexArray(blitVao);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        GL30.glBindVertexArray(prevVao);

        GL20.glUseProgram(prevProgram);
        if (!wasBlend) GL11.glDisable(GL11.GL_BLEND);
        if (wasDepth)  GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private static void ensureBlit() {
        if (blitProgram != 0) return;

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
