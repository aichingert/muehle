package at.htlleonding.mill.model;

import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.model.helper.LoginHelper;

public class Game {
    Long gameId;
    Long winnerId;
    Long loserId;

    public Game(Long gameId, Long winnerId, Long loserId) {
        this.gameId = gameId;
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    public Game(Long winnerId, Long loserId) {
        this.winnerId = winnerId;
        this.loserId = loserId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public Long getLoserId() {
        return loserId;
    }

    public void setLoserId(Long loserId) {
        this.loserId = loserId;
    }

    @Override
    public String toString() {
        UserRepository userRepository = new UserRepository();

        if (LoginHelper.getInstance().getCurrentUserId().equals(winnerId)) {
            return "WIN against " + userRepository.findById(loserId).getAlias();
        }
        return "LOSE against " + userRepository.findById(winnerId).getAlias();
    }
}
