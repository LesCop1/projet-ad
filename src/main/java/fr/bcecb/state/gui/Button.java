package fr.bcecb.state.gui;

import fr.bcecb.event.MouseEvent;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.Texture;

public class Button extends GuiElement {
    private String title;
    private ResourceHandle<Texture> onHoverTexture = new ResourceHandle<>("textures/defaultButtonHover.png") {};
    private ResourceHandle<Texture> texture = new ResourceHandle<>("textures/defaultButton.png") {};

    public Button(int id, float x, float y, float width, float height) {
        this(id, x, y, width, height, false);
    }

    public Button(int id, float x, float y, float width, float height, boolean centered) {
        this(id, x, y, width, height, centered, null, null);
    }

    public Button(int id, float x, float y, float width, float height, String title) {
        this(id, x, y, width, height, false, title, null);
    }

    public Button(int id, float x, float y, float width, float height, ResourceHandle<Texture> texture) {
        this(id, x, y, width, height, false, null, texture);
    }

    public Button(int id, float x, float y, float width, float height, boolean centered, String title) {
        this(id, x, y, width, height, centered, title, null);
    }

    public Button(int id, float x, float y, float width, float height, String title, ResourceHandle<Texture> texture) {
        this(id, x, y, width, height, false, title, texture);
    }

    public Button(int id, float x, float y, float width, float height, boolean centered, ResourceHandle<Texture> texture) {
        this(id, x, y, width, height, centered, null, texture);
    }

    public Button(int id, float x, float y, float width, float height, boolean centered, String title, ResourceHandle<Texture> texture) {
        this(id, x, y, width, height, centered, title, texture, texture);
    }

    public Button(int id, float x, float y, float width, float height, boolean centered, String title, ResourceHandle<Texture> texture, ResourceHandle<Texture> onHoverTexture) {
        super(id, x - (centered ? (width / 2) : 0), y - (centered ? (height / 2) : 0), width, height);
        this.title = title;
        if (texture != null) {
            this.texture = texture;
        }
        if (onHoverTexture != null) {
            this.onHoverTexture = onHoverTexture;
        }
    }

    @Override
    public void onClick(MouseEvent.Click event) {}

    @Override
    public void onHover(MouseEvent.Move event) {}

    @Override
    public void onScroll(MouseEvent.Scroll event) {}

    @Override
    public void setHovered(boolean hovered) {
        super.setHovered(hovered);


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResourceHandle<Texture> getTexture() {
        return texture;
    }

    public void setTexture(ResourceHandle<Texture> texture) {
        this.texture = texture;
    }

    public ResourceHandle<Texture> getOnHoverTexture() {
        return onHoverTexture;
    }

    public void setOnHoverTexture(ResourceHandle<Texture> onHoverTexture) {
        this.onHoverTexture = onHoverTexture;
    }

}