package fr.bcecb.state.gui;

import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.Texture;

public class CircleImage extends Image {
    private float radius;

    public CircleImage(int id, ResourceHandle<Texture> image, float x, float y, float radius) {
        super(id, image, x, y, radius * 2, radius * 2, false);
        this.radius = radius;
    }

    @Override
    boolean checkBounds(float x, float y) {
        return super.checkBounds(x, y) && Math.pow((getX() + getRadius()) - x, 2) + Math.pow((getY() + getRadius()) - y, 2) < Math.pow(getRadius(), 2);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
