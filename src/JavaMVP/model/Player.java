package JavaMVP.model;

import enums.PieceColor;

public class Player {
    private int playerID;
    private String username;
    private String password;
    private PlayerStats playerStats;
    private boolean dropInitiative;

    public Player(int playerID, String username, String password, PlayerStats playerStats, boolean dropInitiative) {
        this.playerID = playerID;
        this.username = username;
        this.password = password;
        this.playerStats = playerStats;
        this.dropInitiative = dropInitiative;
    }

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Player() {

    }

    public int getPlayerID() {
        return playerID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
