package at.htlleonding.mill.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Player {
    public static final int MAX_PIECES = 9;
    private SimpleIntegerProperty amountOfPieces;
    private final int color;
    private SimpleBooleanProperty isPlayerTurn;

    public Player(int color) {
        this.amountOfPieces = new SimpleIntegerProperty(0);
        this.color = color;
        this.isPlayerTurn = new SimpleBooleanProperty(color == 1);
    }

    public int getColor() {
        return this.color;
    }

    public SimpleIntegerProperty amountOfPiecesProperty() {
        return amountOfPieces;
    }

    public int getAmountOfPieces() {
        return this.amountOfPieces.get();
    }

    public void setPiece() {
        this.amountOfPieces.set(this.amountOfPieces.get() + 1);
    }
    public void removePiece() {
        this.amountOfPieces.set(this.amountOfPieces.get() - 1);
    }

    public SimpleBooleanProperty isPlayerTurnProperty() {
        return isPlayerTurn;
    }

    public boolean isPlayerTurn() {
        return this.isPlayerTurn.get();
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.isPlayerTurn.set(playerTurn);
    }

    public Player copy() {
        Player player = new Player(this.color);
        player.isPlayerTurn = this.isPlayerTurn;
        player.amountOfPieces = this.amountOfPieces;
        return player;
    }
}
