package at.htlleonding.mill.model;

import at.htlleonding.mill.repositories.UserRepository;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class User {
    private Long id;
    private String username;
    private String password;
    private String alias;
    private SimpleIntegerProperty currentRank;
    private int winCount;

    public User(String username, String password, String alias) {
        this.username = username;
        this.password = password;
        this.alias = alias;
        currentRank = new SimpleIntegerProperty();
    }

    public User(String username, String password, String alias, int rank, int winCount) {
        this.username = username;
        this.password = password;
        this.alias = alias;
        currentRank = new SimpleIntegerProperty(rank);
        this.winCount = winCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return currentRank.get() + " " + username + " (" + winCount + " W)";
    }
}
