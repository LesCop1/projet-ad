package fr.bcecb.resources;

import fr.bcecb.util.Log;
import fr.bcecb.util.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture extends LWJGLResource {
    protected int width;
    protected int height;

    public Texture() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, getLWJGLId());
    }

    @Override
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    @Override
    public int create(InputStream inputStream) throws IOException {
        ByteBuffer image;
        int[] width = new int[1];
        int[] height = new int[1];
        int[] channels = new int[1];

        ByteBuffer imageBuffer = Resources.readByteBuffer(inputStream);
        image = stbi_load_from_memory(imageBuffer, width, height, channels, 4);

        if (image == null) {
            Log.SYSTEM.warning("Couldn't load texture : " + stbi_failure_reason());
            return 0;
        }

        this.width = width[0];
        this.height = height[0];

        int texture = generate(image, GL_RGBA);
        stbi_image_free(image);
        return texture;
    }

    public int generate(ByteBuffer image, int format) {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image);
        glGenerateMipmap(GL_TEXTURE_2D);

        return texture;
    }

    @Override
    public void dispose() {
        glDeleteTextures(getLWJGLId());
    }
}
