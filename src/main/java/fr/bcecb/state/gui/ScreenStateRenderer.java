package fr.bcecb.state.gui;

import fr.bcecb.render.RenderManager;
import fr.bcecb.render.Renderer;
import fr.bcecb.render.Window;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.Texture;
import fr.bcecb.util.Log;

public class ScreenStateRenderer extends Renderer<ScreenState> {
    public ScreenStateRenderer(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceHandle<Texture> getTexture(ScreenState state) {
        return state.getBackgroundTexture();
    }

    @Override
    public void render(ScreenState state, float partialTick) {
        ResourceHandle<Texture> textureResourceHandle = getTexture(state);

        renderManager.getRenderEngine().drawTexturedRect(textureResourceHandle, 0, 0, Window.getCurrentWindow().getWidth(), Window.getCurrentWindow().getHeight());

        for (GuiElement element : state.getGuiElements()) {
            Renderer<GuiElement> guiElementRenderer = renderManager.getRendererFor(element);

            if (guiElementRenderer != null) {
                guiElementRenderer.render(element, partialTick);
            } else {
                Log.RENDER.warning("GUI element {0} in {1} has no renderer !", element.getClass().getSimpleName(), state.getName());
            }
        }
    }
}