package fr.bcecb.state.gui;

import com.google.common.base.Strings;
import fr.bcecb.event.MouseEvent;
import fr.bcecb.render.RenderEngine;
import fr.bcecb.render.RenderManager;
import fr.bcecb.render.Renderer;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.ResourceManager;
import fr.bcecb.resources.Texture;
import org.joml.Vector4f;

public class Button extends GuiElement {
    private String title;
    private ResourceHandle<Texture> onHoverTexture = new ResourceHandle<>("textures/defaultButtonHover.png") {
    };
    private ResourceHandle<Texture> texture = new ResourceHandle<>("textures/defaultButton.png") {
    };
    private float ticksHovered = 0;

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

    public float getHoveredTicks() {
        return ticksHovered;
    }

    public void incHoveredTicks() {
        this.ticksHovered++;
    }

    public void decHoveredTicks() {
        if (this.ticksHovered != 0) {
            this.ticksHovered--;
        }
    }

    public void setTicksHovered(float ticksHovered) {
        this.ticksHovered = ticksHovered;
    }

    @Override
    public void onClick(MouseEvent.Click event) {
    }

    @Override
    public void onHover(MouseEvent.Move event) {

    }

    @Override
    public void onScroll(MouseEvent.Scroll event) {

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

    public static class ButtonRenderer extends Renderer<Button> {

        public ButtonRenderer(RenderManager renderManager) {
            super(renderManager);
        }

        @Override
        public ResourceHandle<Texture> getTexture(Button button) {
            if (button.isHovered()) {
                return button.getOnHoverTexture();
            } else {
                return button.getTexture();
            }
        }

        @Override
        public void render(Button button, float partialTick) {
            RenderEngine renderEngine = renderManager.getRenderEngine();

            float effect = bouncyEffect(button, 5, 5, 3);

            renderEngine.drawTexturedRect(getTexture(button), button.getX() - effect, button.getY() - effect, button.getX() + button.getWidth() + effect, button.getY() + button.getHeight() + effect);

            if (!Strings.isNullOrEmpty(button.getTitle())) {
                renderEngine.drawCenteredText(ResourceManager.DEFAULT_FONT, button.getTitle(), button.getX() + (button.getWidth() / 2.0f), button.getY() + (button.getHeight() / 2.0f), 1.0f, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
            }
        }

        private float bouncyEffect(Button button, int offset, int bouncePower, int bounceSpeed) {
            boolean bounce = true;
            if (button.isHovered()) {
                if (button.getHoveredTicks() < (180 / ((float) bounceSpeed)) + offset) {
                    button.incHoveredTicks();
                } else {
                    button.setTicksHovered(offset);
                }
            } else {
                bounce = false;
                if (button.getHoveredTicks() < (offset + bouncePower)) {
                    button.decHoveredTicks();
                } else {
                    button.setTicksHovered((float) Math.floor(offset + (bouncePower * Math.sin(Math.toRadians((button.getHoveredTicks() - offset) * bounceSpeed)))));
                }
            }

            if ((button.getHoveredTicks() < offset) || !bounce) {
                return button.getHoveredTicks();
            } else {
                return (float) (offset + (bouncePower * Math.sin(Math.toRadians((button.getHoveredTicks() - offset) * bounceSpeed))));
            }
        }
    }
}