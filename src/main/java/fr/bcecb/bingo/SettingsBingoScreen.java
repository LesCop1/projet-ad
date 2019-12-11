package fr.bcecb.bingo;

import fr.bcecb.input.MouseButton;
import fr.bcecb.state.StateManager;
import fr.bcecb.state.gui.Button;
import fr.bcecb.state.gui.GuiElement;
import fr.bcecb.state.gui.ScreenState;
import fr.bcecb.state.gui.Text;

public class SettingsBingoScreen extends ScreenState {
    private int gridCount;
    private int difficulty;

    private Button startButton;

    public SettingsBingoScreen(StateManager stateManager) {
        super(stateManager, "settings_bingo");
        this.gridCount = 0;
        this.difficulty = 0;
    }

    @Override
    public void initGui() {
        int id = 0;

        GuiElement gridCountText = new Text(id++, (width / 10f), (height / 6f) + 10, false, "Grid count :");
        for (int i = 0; i < 6; i++) {
            Button gridCountButton = new GridCountSettingButton(id++, (width / 10f) + i * (width / 10f), (height / 6f) + 35, i + 1);
            addGuiElement(gridCountButton);
        }

        GuiElement difficultyText = new Text(id++, (width / 10f), (height / 3f) + 40, false, "Difficulty :");
        for (int i = 0; i < 3; i++) {
            Button difficultyButton = new DifficultySettingButton(id++, (width / 10f) + i * (width / 10f), 2 * (height / 6f) + 65, i + 1);
            addGuiElement(difficultyButton);
        }

        this.startButton = new Button(++id, (width / 2f), (height / 4f) + 2 * (height / 4f), (width / 5f), (height / 10f), true, "Start") {
            @Override
            public boolean isDisabled() {
                return gridCount == 0 || difficulty == 0;
            }
        };

        Button backButton = new Button(BACK_BUTTON_ID, (width / 20f), (height - (height / 20f) - (height / 10f)), (height / 10f), (height / 10f), false, "Back");

        addGuiElement(gridCountText, difficultyText, startButton, backButton);
    }

    @Override
    public boolean mouseClicked(int id, MouseButton button) {
        if (id == this.startButton.getId()) {
            stateManager.pushState(new BingoScreen(stateManager, this.gridCount, this.difficulty));
            return true;
        } else {
            SettingButton settingButton = (SettingButton) this.getGuiElementById(id);

            if (settingButton instanceof DifficultySettingButton) {
                this.difficulty = settingButton.value;
            } else if (settingButton instanceof GridCountSettingButton) {
                this.gridCount = settingButton.value;
            }
        }

        return false;
    }

    private static class SettingButton extends Button {
        protected final int value;

        public SettingButton(int id, float x, float y, int value) {
            super(id, x, y, 30, 30, true);
            this.value = value;
        }

        @Override
        public String getTitle() {
            return String.valueOf(value);
        }
    }

    private final class DifficultySettingButton extends SettingButton {
        public DifficultySettingButton(int id, float x, float y, int value) {
            super(id, x, y, value);
        }

        @Override
        public boolean isDisabled() {
            return SettingsBingoScreen.this.difficulty == value;
        }
    }

    private final class GridCountSettingButton extends SettingButton {
        private final int value;

        public GridCountSettingButton(int id, float x, float y, int value) {
            super(id, x, y, value);
            this.value = value;
        }

        @Override
        public boolean isDisabled() {
            return SettingsBingoScreen.this.gridCount == value;
        }
    }
}
