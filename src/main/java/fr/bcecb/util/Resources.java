package fr.bcecb.util;

import fr.bcecb.resources.*;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.stream.Collectors;

public class Resources {
    /* TEXTURES */
    public static final ResourceHandle<Texture> DEFAULT_TEXTURE = new ResourceHandle<>("textures/default/default.png") {
    };
    public static final ResourceHandle<Texture> DEFAULT_START_BACKGROUND_TEXTURE = new ResourceHandle<>("textures/default/defaultStartBackGround.png") {
    };
    public static final ResourceHandle<Texture> DEFAULT_BACKGROUND_TEXTURE = new ResourceHandle<>("textures/default/defaultBackground.png") {
    };
    public static final ResourceHandle<Texture> DEFAULT_BUTTON_TEXTURE = new ResourceHandle<>("textures/default/defaultButton.png") {};
    public static final ResourceHandle<Texture> DEFAULT_BUTTON_HOVER_TEXTURE = new ResourceHandle<>("textures/default/defaultButtonHover.png") {};
    public static final ResourceHandle<Texture> DEFAULT_BUTTON_DISABLED_TEXTURE = new ResourceHandle<>("textures/default/defaultButtonDisabled.png") {};
    public static final ResourceHandle<Texture> DEFAULT_BUTTON_STATS_TEXTURE = new ResourceHandle<>("textures/statsProfile.png") {};
    public static final ResourceHandle<Texture> CURRENT_PROFILE_TEXTURE = new ResourceHandle<>("textures/default/defaultProfile.jpg") {
    };
    public static final ResourceHandle<Texture> DEFAULT_BUTTON_CONFIRMED = new ResourceHandle<>("textures/default/defaultButtonConfirmed.png") {
    };


    public static final ResourceHandle<Font> DEFAULT_FONT = new ResourceHandle<>("font.ttf") {};
    /* SHADERS */
    public static final ResourceHandle<Shader> DEFAULT_SHADER = new ResourceHandle<>("shaders/base.json") {
    };
    public static final ResourceHandle<Shader> CIRCLE_SHADER = new ResourceHandle<>("shaders/circle.json") {
    };
    public static final ResourceHandle<Shader> ROUNDED_SHADER = new ResourceHandle<>("shaders/rounded.json") {
    };
    public static final ResourceHandle<Shader> FONT_SHADER = new ResourceHandle<>("shaders/font.json") {
    };

    public static final ResourceHandle<Profile> DEFAULT_PROFILE_FILE = new ResourceHandle<>("defaultprofile.json") {
    };

    public static ByteBuffer readByteBuffer(InputStream inputStream) throws IOException {
        ByteBuffer buffer = BufferUtils.createByteBuffer(inputStream.available());
        buffer.put(inputStream.readAllBytes());
        buffer.flip();

        return buffer;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }

    public static String readResource(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
