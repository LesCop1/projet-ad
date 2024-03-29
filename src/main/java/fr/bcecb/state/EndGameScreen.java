package fr.bcecb.state;

import fr.bcecb.Game;
import fr.bcecb.input.MouseButton;
import fr.bcecb.state.gui.*;
import fr.bcecb.util.Constants;

import java.util.concurrent.TimeUnit;

public class EndGameScreen extends PopUpScreenState {
    private GuiElement replayButton;
    private GuiElement exitButton;

    private Constants.GameType gameType;
    private long time;
    private int moneyEarned;

    public EndGameScreen(StateManager stateManager, Constants.GameType gameType, long time, int moneyEarned) {
        super(stateManager, "end_game", 200, 150, Constants.COLOR_GREY);
        this.gameType = gameType;
        this.time = time;
        this.moneyEarned = moneyEarned;

        if (Game.instance().getProfile().getRecord(gameType) > this.time) {
            Game.instance().getProfile().setRecord(gameType, this.time);
        }
        Game.instance().getProfile().setMoneyAmount((long) (Game.instance().getProfile().getMoneyAmount() + this.moneyEarned));

        Constants.BANKROLL += this.moneyEarned;
    }

    @Override
    public void initGui() {
        Text gameTypeTitle = new Text(1001, (width / 2f), (height / 2f) - 65f, true, this.gameType.getName() + " terminé !");

        Text timeTitle = new Text(1002, (width / 2f) - 80f, (height / 2f) - 40f, false, "Votre temps :", 0.7f);
        Text moneyTitle = new Text(1003, (width / 2f) - 80f, (height / 2f) - 25f, false, "Votre argent gagné :", 0.7f);

        Text time = new Text(1015, (width / 2f) + 50f, (height / 2f) - 40f, false, longToTime(this.time), 0.7f);
        Text money = new Text(1005, (width / 2f) + 50f, (height / 2f) - 25f, false, intToMoney(this.moneyEarned), 0.7f);

        RoundedRectangle lineSeparation = new RoundedRectangle(1004, (width / 2f), (height / 2f) - 15f, 180, 1, true, Constants.COLOR_GREY, Float.MAX_VALUE);

        Text bestTimeTitle = new Text(1006, (width / 2f) - 80f, (height / 2f), false, "Meilleur temps :", 0.6f);
        Text allMoneyTitle = new Text(1007, (width / 2f) - 80f, (height / 2f) + 10f, false, "Votre argent :", 0.6f);

        Text bestTime = new Text(1008, (width / 2f) + 50f, (height / 2f), false, findBestTime(this.gameType), 0.6f);
        Text bankroll = new Text(1009, (width / 2f) + 50f, (height / 2f) + 10f, false, intToMoney(Game.instance().getProfile().getMoneyAmount()), 0.6f);

        RoundedRectangle lineSeparation2 = new RoundedRectangle(1010, (width / 2f), (height / 2f) + 20f, 180, 1, true, Constants.COLOR_GREY, Float.MAX_VALUE);

        this.replayButton = new Button(1011, (width / 2f) - (backgroundWidth / 4f), (height / 2f) + 45f, 80, 35, true, "Rejouer");

        this.exitButton = new Button(1012, (width / 2f) + (backgroundWidth / 4f), (height / 2f) + 45f, 80, 35, true, "Quitter");

        addGuiElement(gameTypeTitle, timeTitle, time, moneyTitle, money, lineSeparation, bestTimeTitle, allMoneyTitle, bestTime, bankroll, lineSeparation2, replayButton, exitButton);
    }

    @Override
    public boolean shouldRenderBelow() {
        return true;
    }

    private String findBestTime(Constants.GameType gameType) {
        long bestTime = Game.instance().getProfile().getRecordDisplay(gameType);
        return longToTime(bestTime);
    }

    private String longToTime(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    private String intToMoney(int money) {
        return money + " " + Constants.MONEY_NAME_SHORT;
    }

    @Override
    public boolean mouseClicked(int id, MouseButton button) {
        if (id == replayButton.getId()) {
            stateManager.popMultipleState(gameType.getNbState());
            return true;
        } else if (id == exitButton.getId()) {
            stateManager.popMultipleState(gameType.getNbState() + 2);
            return true;
        }
        return false;
    }
}
