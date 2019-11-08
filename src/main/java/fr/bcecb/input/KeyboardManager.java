package fr.bcecb.input;

import com.google.common.collect.Maps;
import fr.bcecb.Game;
import fr.bcecb.event.Event;
import fr.bcecb.event.KeyboardEvent;
import fr.bcecb.render.Window;

import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardManager {
    private final Map<Key, Boolean> keyStates = Maps.newEnumMap(Key.class);

    public KeyboardManager(Window window) {
        glfwSetKeyCallback(window.getId(), this::keyCallback);
    }

    public boolean isKeyDown(Key key) {
        return keyStates.getOrDefault(key, false);
    }

    private void keyCallback(long window, int keyCode, int scancode, int action, int mods) {
        Key key = Key.valueOf(keyCode);

        if (key != Key.UNKNOWN) {
            keyStates.put(key, action == GLFW_PRESS);
        }

        Event event = null;

        if (action == GLFW_RELEASE)
            event = new KeyboardEvent.Release(key);
        else if (action == GLFW_PRESS)
            event = new KeyboardEvent.Press(key);
        else if (action == GLFW_REPEAT)
            event = new KeyboardEvent.Repeat(key);

        if (event != null) {
            Game.EVENT_BUS.post(event);
        }
    }
}