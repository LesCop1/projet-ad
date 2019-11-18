package fr.bcecb.batailleNavale;

import fr.bcecb.Game;
import fr.bcecb.event.KeyboardEvent;
import fr.bcecb.event.MouseEvent;
import fr.bcecb.input.Key;
import fr.bcecb.render.Window;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.state.gui.Button;
import fr.bcecb.state.gui.GuiElement;
import fr.bcecb.state.gui.ScreenState;
import org.lwjgl.glfw.GLFW;
import fr.bcecb.resources.Texture;

import java.util.ArrayList;
import java.util.List;

public class FirstPhaseBattleshipScreen extends ScreenState {
    private static final ResourceHandle<Texture> defaultTexture = new ResourceHandle<>("textures/BatailleNavale/caseBattleship.png") {};

    private Battleship battleship = new Battleship();
//    private Battleship battleship;
    private Boat boat;

    private List<String> boatPut = new ArrayList<>();

    private int selectedX = -1;
    private int selectedY = -1;

    private String selectedBoat = "";

    private String changeOrientation = "Horizontale";

//    public FirstPhaseBattleshipScreen(Battleship battleship) {
//        super("game-battleship.firstphase");
//        this.battleship = battleship;
//
//        setBackgroundTexture(new ResourceHandle<>("textures/battleshipScreen.png") {});
//    }

    public FirstPhaseBattleshipScreen() {
        super("game-battleship.firstphase");
        setBackgroundTexture(new ResourceHandle<>("textures/battleshipScreen.png") {});
    }

    @Override
    public void initGui() {
        battleship.initGrid();

        setBackgroundTexture(new ResourceHandle<>("textures/BatailleNavale/background_battleship.jpg") {
        });
        if(Game.instance().getInputManager().isKeyDown(Key.BATTLESHIP_SWAP_H)) battleship.swapOrientation(boat);
        Button caseButton;
        int id = 1;
        float btnSize = 25f;
        float x = (width / 2f) - (9 * btnSize / 2);
        for (int i = 0; i < 10; ++i, x += btnSize) {
            float y = (height / 2f) - (9 * btnSize / 2);
            for (int j = 0; j < 10; ++j, ++id, y += btnSize) {
                int caseX = i;
                int caseY = j;
                caseButton = new Button(id, x, y, btnSize, btnSize, false) {
                    @Override
                    public void onClick(MouseEvent.Click event) {
                        super.onClick(event);

                        if (selectedBoat != "" && !battleship.cannotPlace(boat, caseX, caseY)
                                && !boatPut.contains(selectedBoat)) {
                            boatPut.add(selectedBoat);
//                            setTitle(selectedBoat);
                            battleship.putBoat(boat, caseX, caseY);
                            selectedBoat = "";
                        }
                    }

                    @Override
                    public boolean isDisabled() {
                        return battleship.getCurrentPlayerBoard()[caseX][caseY] != 0;
                    }

                    @Override
                    public ResourceHandle<Texture> getTexture() {
                        return battleship.getCurrentPlayerBoard()[caseX][caseY] != 0 ? whichTexture(battleship.getCurrentPlayerBoard()[caseX][caseY]) : defaultTexture;
                    }

                    public ResourceHandle<Texture> whichTexture(int num) {
                        if(num == 5) return new ResourceHandle<>("textures/BatailleNavale/A.png") {};
                        if(num == 4) return new ResourceHandle<>("textures/BatailleNavale/C.png") {};
                        if(num == 31) return new ResourceHandle<>("textures/BatailleNavale/F.png") {};
                        if(num == 30) return new ResourceHandle<>("textures/BatailleNavale/S.png") {};
                        if(num == 2) return new ResourceHandle<>("textures/BatailleNavale/T.png") {};
                        return defaultTexture;
                    }
                };
                addGuiElement(caseButton);
            }
        }

        final GuiElement backButton = new Button(999, 0, 0, 50 / ((float) 1920 / width), 50 / ((float) 1920 / width), false, new ResourceHandle<>("textures/back_button.png") {
        }) {
            @Override
            public void onClick(MouseEvent.Click event) {
//                Game.instance().getStateEngine().popState();
//                Game.instance().getStateEngine().popState();
            }
        };
        addGuiElement(backButton);

        Button a = new Button(102, (width / 20f), 50, (height / 10f), (height / 10f), false, "A",
                new ResourceHandle<Texture>("textures/BatailleNavale/A.png") {
                }) {
            @Override
            public void onClick(MouseEvent.Click event) {
                super.onClick(event);
                selectedBoat = getTitle();
                boat = new Boat(Boat.Type.AIRCRAFT_CARRIER);
            }
        };
        addGuiElement(a);

        Button c = new Button(103, (width / 20f), 100, (height / 10f), (height / 10f), false, "C",
                new ResourceHandle<Texture>("textures/BatailleNavale/C.png") {
                }) {
            @Override
            public void onClick(MouseEvent.Click event) {
                super.onClick(event);
                selectedBoat = getTitle();
                boat = new Boat(Boat.Type.CRUISER);
            }
        };
        addGuiElement(c);

        Button f = new Button(104, (width / 20f), 150, (height / 10f), (height / 10f), false, "F",
                new ResourceHandle<Texture>("textures/BatailleNavale/F.png") {
                }) {
            @Override
            public void onClick(MouseEvent.Click event) {
                super.onClick(event);
                selectedBoat = getTitle();
                boat = new Boat(Boat.Type.FRIGATE);
            }
        };
        addGuiElement(f);

        Button s = new Button(105, (width / 20f), 200, (height / 10f), (height / 10f), false, "S",
                new ResourceHandle<Texture>("textures/BatailleNavale/S.png") {
                }) {
            @Override
            public void onClick(MouseEvent.Click event) {
                super.onClick(event);
                selectedBoat = getTitle();
                boat = new Boat(Boat.Type.SUBMARINE);
            }
        };
        addGuiElement(s);

        Button t = new Button(106, (width / 20f), 250, (height / 10f), (height / 10f), false, "T",
                new ResourceHandle<Texture>("textures/BatailleNavale/T.png") {
                }) {
            @Override
            public void onClick(MouseEvent.Click event) {
                super.onClick(event);
                selectedBoat = getTitle();
                boat = new Boat(Boat.Type.TORPEDO);
            }
        };
        addGuiElement(t);
    }

    public void clickButton(int id, MouseEvent.Click event) {
    }
}
