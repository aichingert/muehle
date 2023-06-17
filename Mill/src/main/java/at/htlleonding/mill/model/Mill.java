package at.htlleonding.mill.model;

import at.htlleonding.mill.model.helper.Position;
import at.htlleonding.mill.model.helper.Logic;

import java.util.List;

public class Mill {
    public final int BOARD_SIZE = 3;
    private int[][][] board;
    private int moveCounter;
    private GameState gameState = GameState.SET;
    private final Player playerOne;
    private final Player playerTwo;

    public Mill(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
        this.moveCounter = 0;
    }

    public boolean setPiece(int color, Position position) {
        if (color != 1 && color != 2) {
            return false;
        }

        if ((this.board[position.getZ()][position.getY()][position.getX()] != 0) ||
                (position.getX() == 1 && position.getY() == 1)) {
            return false;
        }

        this.board[position.getZ()][position.getY()][position.getX()] = color;

        if (this.playerOne.getColor() == color) {
            this.playerOne.setPiece();
        } else {
            this.playerTwo.setPiece();
        }

        return true;
    }

    public void removePiece(Position position) {
        if (gameState == GameState.TAKE && getValueAt(position) == (getCurrentPlayerColor() == 1 ? 2 : 1)) {
            if (this.getCurrentPlayerColor() == this.playerOne.getColor()) {
                this.playerTwo.removePiece();
            } else {
                this.playerOne.removePiece();
            }
            this.board[position.getZ()][position.getY()][position.getX()] = 0;
        }
    }

    public boolean movePiece(int color, Position from, Position to) {
        // Note: There is no need to check if the current position is the same color as the piece we want to move
        // because we can only select pieces that have our color
        if (from.equals(to)) {
            return false;
        }

        List<Position> possibleMoves = Logic.getMoves(this, from);

        if (this.gameState == GameState.JUMP && this.getValueAt(to) != 0
        || this.gameState != GameState.JUMP && !possibleMoves.contains(to)) {
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
        this.moveCounter += 1;

        this.playerOne.setPlayerTurn(!this.playerOne.isPlayerTurn());
        this.playerTwo.setPlayerTurn(!this.playerTwo.isPlayerTurn());

        this.updateGameState();
    }

    public void updateGameState() {
        if (this.moveCounter < 2 * Player.MAX_PIECES) {
            this.gameState = GameState.SET;
        } else if (this.playerOne.getAmountOfPieces() < 3 || this.playerTwo.getAmountOfPieces() < 3) {
            this.gameState = GameState.OVER;
        } else if (this.getCurrentPlayerColor() == 1 && this.playerOne.getAmountOfPieces() == 3
                || this.getCurrentPlayerColor() == 2 && this.playerTwo.getAmountOfPieces() == 3) {
            this.gameState = GameState.JUMP;
        } else {
            this.gameState = GameState.MOVE;
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public Mill copy() {
        Mill cp = new Mill(this.playerOne.copy(), this.playerTwo.copy());
        cp.moveCounter = this.moveCounter;
        cp.gameState = this.gameState;
        cp.board = this.board;
        return cp;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
