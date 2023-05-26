package at.htlleonding.mill.model;

public class Game {
    Long gameId;
    Long user1Id;
    Long user2Id;

    public Game(Long gameId, Long user1Id, Long user2Id) {
        this.gameId = gameId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }
}
