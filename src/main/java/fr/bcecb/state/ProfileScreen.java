package fr.bcecb.state;

import fr.bcecb.Game;
import fr.bcecb.input.MouseButton;
import fr.bcecb.resources.Profile;
import fr.bcecb.resources.ResourceHandle;
import fr.bcecb.resources.Texture;
import fr.bcecb.state.gui.*;
import fr.bcecb.util.Constants;
import fr.bcecb.util.Resources;

import java.util.List;

public class ProfileScreen extends ScreenState {
    public ProfileScreen(StateManager stateManager) {
        super(stateManager, "profile_menu");
    }

    @Override
    public void initGui() {
        CircleButton profileButton = new CircleButton(50, width / 2.0f, height / 2.0f, (height / 10f), true) {
            @Override
            public ResourceHandle<Texture> getTexture() {
                return Resources.CURRENT_PROFILE_TEXTURE;
            }

            @Override
            public ResourceHandle<Texture> getHoverTexture() {
                return null;
            }
        };

        Profile profile = Game.instance().getProfile();
        Text name = new Text(51, width / 2f, (height / 2.0f) - 2 * ((height / 7f)), true, profile.getName());
        Text nbSkin = new Text(52, width / 2f, (height / 2.0f) + 2 * ((height / 14f)), true, "Items : " + profile.getItemsOwns().size());
        Text bankRoll = new Text(53, width / 2f, (height / 2.0f) - 2 * ((height / 13f)), true, "Money : " + profile.getMoneyAmount());
        Text nbSuccess = new Text(54, width / 2f, (height / 2.0f) + 2 * ((height / 9f)), true, "Achievements : " + profile.getAchievementsSucceed().values().stream().mapToInt(List::size).sum());

        Button backButton = new Button(BACK_BUTTON_ID, (width / 20f), (height - (height / 20f) - (height / 10f)), (height / 10f), (height / 10f), false, "Back");
        int jeu = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                float centerX, centerY, radius;
                radius = profileButton.getRadius() * 1.7f;
                centerX = i * (width / 2f) + (width / 4f);
                centerY = j * (height / 2f) + (height / 4f);

                drawCircle(centerX, centerY, radius, i, j);
                drawGameStats(jeu, centerX, centerY, radius);
                jeu++;
            }
        }
        addGuiElement(profileButton, backButton, bankRoll,nbSuccess,name,nbSkin);
    }

    @Override
    public boolean mouseClicked(int id, MouseButton button) {
        if (id == BACK_BUTTON_ID) {
            stateManager.popState();
            return true;
        }
        return false;
    }

    public void drawCircle(float centerX, float centerY, float radius, int iCircle, int jCircle) {
        GuiElement score = new CircleImage(-(300 * iCircle + jCircle + 300), Resources.DEFAULT_BUTTON_STATS_TEXTURE, centerX - radius, centerY - radius, radius);
        addGuiElement(score);
    }

    private void drawGameStats(int GameID, float centerX, float centerY, float radius) {
        int id = GameID * 10;
        Constants.GameType gameType;
        switch (GameID) {
            case 0:
                gameType = Constants.GameType.SUDOKU;
                break;
            case 1:
                gameType = Constants.GameType.BINGO;
                break;
            case 2:
                gameType = Constants.GameType.BATTLESHIP;
                break;
            case 3:
                gameType = Constants.GameType.POKER;
                break;
            default:
                return;
        }
        Profile profile = Game.instance().getProfile();
        Text gameName = new Text(id++, centerX, (centerY - 4 * radius / 8f), true, gameType.getName(), 0.9f);
        Text gameBestScore = new Text(id++, centerX, centerY, true, "Best time :" + profile.getRecord(gameType), 0.5f);
        addGuiElement(gameName, gameBestScore);

    }
}
