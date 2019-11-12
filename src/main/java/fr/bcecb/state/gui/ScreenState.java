package fr.bcecb.state.gui;

import com.google.common.collect.Sets;
import fr.bcecb.Game;
import fr.bcecb.event.MouseEvent;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.Texture;
import fr.bcecb.state.State;
import fr.bcecb.util.Constants;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import static fr.bcecb.event.MouseEvent.Click.Type.RELEASED;

public abstract class ScreenState extends State {
    private final Set<GuiElement> guiElements = Sets.newTreeSet(Comparator.comparingInt(GuiElement::getId));
    private ResourceHandle<Texture> backgroundTexture = Constants.DEFAULT_TEXTURE;

    protected ScreenState(String name) {
        super(name);
    }

    public abstract void initGui();

    protected final void addGuiElement(GuiElement... elements) {
        guiElements.addAll(Arrays.asList(elements));
    }

    protected final void removeGuiElement(GuiElement element) {
        guiElements.remove(element);
    }

    public final void clearGuiElements() {
        guiElements.clear();
    }

    public void setBackgroundTexture(ResourceHandle<Texture> backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
    }

    public ResourceHandle<Texture> getBackgroundTexture() {
        return backgroundTexture;
    }

    public final Collection<GuiElement> getGuiElements() {
        return guiElements;
    }

    @Override
    public boolean shouldRenderBelow() {
        return false;
    }

    @Override
    public boolean shouldUpdateBelow() {
        return false;
    }

    @Override
    public void onEnter() {
        Game.EVENT_BUS.register(this);
    }

    @Override
    public void onExit() {
        Game.EVENT_BUS.unregister(this);
    }

    @Override
    public void onUpdate() {
        for (GuiElement element : getGuiElements()) {
            element.onUpdate();
        }
    }

    public void onClick(MouseEvent.Click event) {
        for (GuiElement element : getGuiElements()) {
            if (!element.isVisible() || !element.isEnabled()) continue;

            if (!event.isCancelled() && event.getType() == RELEASED && element.checkBounds(event.getX(), event.getY())) {
                element.onClick(event);

                if (element.getClickHandler() != null) {
                    element.getClickHandler().accept(event);
                }

                event.setCancelled(true);
            }
        }
    }

    public void onHover(MouseEvent.Move event) {
        for (GuiElement element : getGuiElements()) {
            if (!element.isVisible()) continue;

            element.setHovered(element.checkBounds(event.getX(), event.getY()));

            if (element.isHovered()) {
                element.onHover(event);

                if (element.getHoverHandler() != null) {
                    element.getHoverHandler().accept(event);
                }

                event.setCancelled(true);
            }
        }
    }

    public void onScroll(MouseEvent.Scroll event) {
        for (GuiElement element : getGuiElements()) {
            if (!element.isVisible() || !element.isEnabled()) continue;

            if (element.isHovered()) {
                element.onScroll(event);

                if (element.getScrollHandler() != null) {
                    element.getScrollHandler().accept(event);
                }

                event.setCancelled(true);
            }
        }
    }
}