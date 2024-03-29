package fr.bcecb.state.gui;

import com.google.common.base.MoreObjects;
import fr.bcecb.util.Constants;
import org.joml.Vector4f;

public class Rectangle extends GuiElement {
    private final Vector4f color;

    public Rectangle(int id, float x, float y, float width, float height, boolean centered) {
        this(id, x, y, width, height, centered, null);
    }

    public Rectangle(int id, float x, float y, float width, float height, boolean centered, Vector4f color) {
        super(id, x - (centered ? (width / 2) : 0), y - (centered ? (height / 2) : 0), width, height);
        this.color = MoreObjects.firstNonNull(color, Constants.COLOR_WHITE);
    }

    @Override
    public void onUpdate() {

    }

    public Vector4f getColor() {
        return color;
    }
}
