package fr.bcecb.batailleNavale;

import fr.bcecb.Game;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.Texture;
import fr.bcecb.state.gui.Button;
import fr.bcecb.state.gui.GuiElement;
import fr.bcecb.state.gui.ScreenState;
import fr.bcecb.Game;


public class BattleshipScreen extends ScreenState {
    private Battleship battleship = new Battleship();

    public BattleshipScreen() {
        super("game-battleship");
    }

    private int clickGrid() {
        return 0; //Case de la grille
    }

    @Override
    public void onEnter() {
        super.onEnter();
        Game.instance().getStateEngine().pushState(new FirstPhaseBattleshipScreen(battleship));
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void initGui() {
        setBackgroundTexture(new ResourceHandle<Texture>("textures/mainMenuBG.png") {});
    }

    @Override
    public boolean shouldRenderBelow() {
        return false;
    }

    @Override
    public boolean shouldUpdateBelow() {
        return false;
    }
}