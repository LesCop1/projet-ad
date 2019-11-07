package fr.bcecb.batailleNavale;

import java.util.Arrays;
import java.util.List;

public class Battleship { //Gère tous les aspects d'une partie, création de la grille, changer l'orientation d'un bateau, le placer, touché/coulé, win condition
    private final Boat[][][] boards = new Boat[2][10][10];
    private int currentPlayer = 0;

    public Boat[][] getCurrentPlayerBoard() {
        return boards[getCurrentPlayer()];
    }

    public Boat[][] getNextPlayerBoard() {
        return boards[getNextPlayer()];
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer() {
        this.currentPlayer = getNextPlayer();
    }

    public int getNextPlayer() {
        return (this.currentPlayer + 1) % 2;
    }

    public void putBoat(Boat boat, int x, int y) { //Place les bateaux
        if (!verification(boat, x, y)) return;
        boat.setPosition(x, y);
        if (!verification(boat, x, y)) {
            return;
        }
        boat.setPosition(x, y);
        if (boat.isHorizontal()) {
            while (x + boat.getSize() >= 10) { //Si on veut placer un bateau sur une case qui fait que le bateau dépasse à gauche
                --x; //On le décale vers la droite
            }
            for (int i = 0; i < boat.getSize(); i++) {
                getCurrentPlayerBoard()[x + i][y] = boat;
            }
        } else {
            while (y + boat.getSize() >= 10) { //Pareil que pour x mais en vertical
                --y;
            }
            for (int i = 0; i < boat.getSize(); i++) {
                getCurrentPlayerBoard()[x][y + i] = boat;
            }
        }
    }

    public boolean verification(Boat boat, int x, int y) {
        if (boat.isHorizontal()) {
            for (int i = 0; i < boat.getSize(); i++) {
                if (x + i >= 10 || getCurrentPlayerBoard()[x + i][y] != null) return false;
            }
        } else {
            for (int i = 0; i < boat.getSize(); i++) {
                if (y + i >= 10 || getCurrentPlayerBoard()[x][y + i] != null) return false;
            }
        }
        return true;
    }

    public void swapOrientation(Boat boat) { //Change l'orientation du bateau passé en adresse
        if (boat.isHorizontal()) boat.setHorizontal(false);
        else boat.setHorizontal(true);
        if (boat.isHorizontal()) boat.setHorizontal(false);
        else boat.setHorizontal(true);
    }

    public boolean shoot(int x, int y) {
        Boat boat = getNextPlayerBoard()[x][y];
        if (boat == null) return false;
        else {
            int hitPosition = boat.isHorizontal() ? boat.getX() - x : boat.getY() - y;
            if (boat.getHits()[hitPosition]) return false;
            else boat.hit(hitPosition);
        }
        if (boat == null) return false;
        else {
            int hitPosition = boat.isHorizontal() ? boat.getX() - x : boat.getY() - y;
            if (boat.getHits()[hitPosition]) return false;
            else boat.hit(hitPosition);
        }
        return true;
    }
}
