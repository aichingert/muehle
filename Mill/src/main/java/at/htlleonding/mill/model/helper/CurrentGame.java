package at.htlleonding.mill.model.helper;

public class CurrentGame {
    Long player1Id;
    Long player2Id;
    private static CurrentGame instance;

    public static CurrentGame getInstance() {
        if (instance == null) {
            instance = new CurrentGame();
        }
        return instance;
    }

    public Long getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Long player1Id) {
        this.player1Id = player1Id;
    }

    public Long getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Long player2Id) {
        this.player2Id = player2Id;
    }
}
