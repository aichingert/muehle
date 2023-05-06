package at.htlleonding.mill.model;

import at.htlleonding.mill.model.helper.Position;
import at.htlleonding.mill.model.helper.Logic;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Mill {
    public final int BOARD_SIZE = 3;
    private int[][][] board;
    private GameState gameState = GameState.SET;

    private Player playerOne;
    private Player playerTwo;

    public Mill(Player playerOne, Player playerTwo) {
        this.playerTwo = playerTwo;
        this.playerOne = playerOne;
        this.board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
    }

    public boolean setPiece(int color, Position position) {
        if (gameState != GameState.SET ||
                color == 1 && this.playerOne.getAmountOfPieces() == 9 ||
                color == 2 && playerTwo.getAmountOfPieces() == 9 ||
                color != 1 && color != 2) {
            return false;
        }

        if ((this.board[position.getZ()][position.getY()][position.getX()] != 0) ||
                (position.getX() == 1 && position.getY() == 1)) {
            return false;
        }

        this.board[position.getZ()][position.getY()][position.getX()] = color;

        switch (color) {
            case 1 -> this.playerOne.setPiece();
            case 2 -> this.playerTwo.setPiece();
        }

        if (this.playerOne.getAmountOfPieces() == 9 && this.playerTwo.getAmountOfPieces() == 9) {
            this.gameState = GameState.MOVE;
        }

        return true;
    }

    public boolean movePiece(int color, Position from, Position to) {
        if (board[from.getZ()][from.getY()][from.getX()] != color || from.equals(to)) {
            return false;
        }

        // 0 0 0
        // 0   0
        // 0 0 0

        List<Position> possibleMoves = Logic.getMoves(this, from);

        if (!possibleMoves.contains(to)) {
            return false;
        }

        board[from.getZ()][from.getY()][from.getX()] = 0;
        board[to.getZ()][to.getY()][to.getX()] = color;
        return true;
    }

    public int getValueAt(Position position) {
        return this.board[position.getZ()][position.getY()][position.getX()];
    }

    public int getCurrentPlayerColor() {
        if (this.playerOne.isPlayerTurn()) {
            return this.playerOne.getColor();
        }

        return this.playerTwo.getColor();
    }

    public void switchTurn() {
        this.playerOne.setPlayerTurn(!this.playerOne.isPlayerTurn());
        this.playerTwo.setPlayerTurn(!this.playerTwo.isPlayerTurn());
    }

    public GameState getGameState() {
        return gameState;
    }
}
