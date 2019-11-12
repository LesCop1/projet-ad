package fr.bcecb.render;

import fr.bcecb.resources.*;
import fr.bcecb.state.StateEngine;
import fr.bcecb.util.Constants;
import fr.bcecb.util.Log;
import fr.bcecb.util.TextRendering;
import fr.bcecb.util.TransformStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static fr.bcecb.util.Constants.COLOR_WHITE;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBTruetype.*;

public class RenderEngine {
    private final ResourceManager resourceManager;
    private final RenderManager renderManager;

    private final Window window;

    private final Tessellator tessellator;
    private final Tessellator textTessellator;

    private final Matrix4f projection = new Matrix4f();
    private final TransformStack transform = new TransformStack(16);

    public RenderEngine(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.renderManager = new RenderManager(this, resourceManager);
        this.window = Window.newInstance("4Games", 800, 600, false);

        GL.createCapabilities();
        Log.RENDER.config("Using OpenGL Version {0}", glGetString(GL_VERSION));

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.tessellator = new Tessellator();
        tessellator.begin(GL_TRIANGLE_FAN);
        tessellator.uv(0.0f, 0.0f).vertex(0.0f, 0.0f);
        tessellator.uv(1.0f, 0.0f).vertex(1.0f, 0.0f);
        tessellator.uv(1.0f, 1.0f).vertex(1.0f, 1.0f);
        tessellator.uv(0.0f, 1.0f).vertex(0.0f, 1.0f);
        tessellator.finish();

        this.textTessellator = new Tessellator();
    }

    public void cleanUp() {
        window.destroy();
        glfwTerminate();
    }

    public void render(StateEngine stateEngine, float partialTick) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        projection.identity();
        projection.ortho2D(0, window.getWidth(), window.getHeight(), 0);

        transform.clear();

        stateEngine.render(renderManager, partialTick);
    }

    public void drawBackground(ResourceHandle<Texture> textureHandle) {
        float width = Window.getCurrentWindow().getWidth();
        float height = Window.getCurrentWindow().getHeight();
        float screenAspectRatio = width / height;

        Texture texture = resourceManager.getResourceOrDefault(textureHandle, Constants.DEFAULT_TEXTURE);
        float imageWidth = texture.getWidth();
        float imageHeight = texture.getHeight();
        float textureAspectRatio = imageWidth / imageHeight;

        float aspectRatio = screenAspectRatio > textureAspectRatio ? width / imageWidth : height / imageHeight;

        transform.pushTransform();
        {
            transform.translate(width / 2.0f, height / 2.0f);
            transform.scale(aspectRatio, aspectRatio);

            drawRect(textureHandle, 0, 0, imageWidth, imageHeight, true);
        }
        transform.popTransform();
    }

    public void drawRect(ResourceHandle<Texture> textureHandle, float minX, float minY, float maxX, float maxY) {
        drawRect(textureHandle, minX, minY, maxX, maxY, false);
    }

    public void drawRect(ResourceHandle<Texture> textureHandle, float minX, float minY, float maxX, float maxY, boolean centered) {
        transform.pushTransform();
        {
            if (centered) {
                float width = Math.abs(maxX - minX);
                float height = Math.abs(maxY - minY);
                transform.translate(-(width / 2), -(height / 2));
            }

            transform.translate(minX, minY);
            transform.scale(maxX - minX, maxY - minY);

            draw(Constants.DEFAULT_SHADER, textureHandle);
        }
        transform.popTransform();
    }

    public void drawCircle(ResourceHandle<Texture> textureHandle, float x, float y, float radius) {
        transform.pushTransform();
        {
            transform.translate(x, y);
            transform.scale(radius * 2, radius * 2);
            draw(Constants.CIRCLE_SHADER, textureHandle);
        }
        transform.popTransform();
    }

    public void drawCenteredText(ResourceHandle<Font> fontHandle, String text, float x, float y, float scale) {
        Font font = resourceManager.getResourceOrDefault(fontHandle, Constants.DEFAULT_FONT);

        if (font != null) {
            float height = ((font.getDescent() - font.getAscent()) / 2.0f) * stbtt_ScaleForPixelHeight(font.getInfo(), 32 * window.getContentScaleY()) * scale;
            float width = TextRendering.getStringWidth(font, text) * scale;

            transform.pushTransform();
            {
                transform.translate(-(width / 2.0f) / window.getContentScaleX(), -(height / 2.0f) / window.getContentScaleY());
                transform.color(Constants.COLOR_BLACK);
                drawText(fontHandle, text, x, y, scale);
            }
            transform.popTransform();
        }
    }

    public void drawText(ResourceHandle<Font> fontHandle, String text, float x, float y, float scale) {
        Shader shader = resourceManager.getResource(Constants.FONT_SHADER);
        Font font = resourceManager.getResourceOrDefault(fontHandle, Constants.DEFAULT_FONT);

        transform.pushTransform();
        {
            if (font != null) {
                transform.translate(x, y);
                transform.scale(scale / window.getContentScaleX(), scale / window.getContentScaleY());

                shader.bind();
                {
                    float pixelScale = stbtt_ScaleForPixelHeight(font.getInfo(), 32 * scale * window.getContentScaleY());
                    font.bind();
                    {
                        try (MemoryStack stack = MemoryStack.stackPush()) {
                            FloatBuffer xBuffer = stack.mallocFloat(1);
                            FloatBuffer yBuffer = stack.mallocFloat(1);
                            STBTTAlignedQuad quad = STBTTAlignedQuad.malloc();

                            for (int i = 0; i < text.length(); ++i) {
                                int cp = text.codePointAt(i);
                                if (cp == '\n') {
                                    yBuffer.put(0, yBuffer.get(0) + (font.getAscent() - font.getDescent() + font.getLineGap()) * pixelScale);
                                    xBuffer.put(0, 0.0f);
                                    continue;
                                }

                                stbtt_GetBakedQuad(font.getCData(), font.getWidth(), font.getHeight(), cp - 32, xBuffer, yBuffer, quad, true);

                                if (i < text.length() - 1) {
                                    xBuffer.put(0, xBuffer.get(0) + stbtt_GetCodepointKernAdvance(font.getInfo(), cp, text.codePointAt(i + 1)) * pixelScale);
                                }

                                textTessellator.begin(GL_TRIANGLE_FAN);
                                textTessellator.color(transform.color.x, transform.color.y, transform.color.z, transform.color.w);
                                textTessellator.uv(quad.s0(), quad.t0()).vertex(quad.x0(), quad.y0());
                                textTessellator.uv(quad.s1(), quad.t0()).vertex(quad.x1(), quad.y0());
                                textTessellator.uv(quad.s1(), quad.t1()).vertex(quad.x1(), quad.y1());
                                textTessellator.uv(quad.s0(), quad.t1()).vertex(quad.x0(), quad.y1());

                                shader.uniformMat4("projection", projection);
                                shader.uniformMat4("model", transform.model);
                                textTessellator.draw();
                                shader.uniformVec4("override_Color", COLOR_WHITE);
                            }
                        }
                    }
                    font.unbind();
                }
                shader.unbind();
            }
        }
        transform.popTransform();
    }

    private void draw(ResourceHandle<Shader> shaderResourceHandle, ResourceHandle<Texture> textureHandle) {
        Texture texture = resourceManager.getResourceOrDefault(textureHandle, Constants.DEFAULT_TEXTURE);
        Shader shader = resourceManager.getResource(shaderResourceHandle);
        texture.bind();
        shader.bind();
        {
            shader.uniformMat4("projection", projection);
            shader.uniformMat4("model", transform.model);
            shader.uniformVec4("override_Color", transform.color);
            tessellator.draw();
            shader.uniformVec4("override_Color", COLOR_WHITE);
        }
        shader.unbind();
        texture.unbind();
    }

    public Window getWindow() {
        return window;
    }

    public TransformStack getTransform() {
        return transform;
    }
}
