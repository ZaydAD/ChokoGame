package JavaMVP.model;

public class PlayerStats {
    private int rank;
    private String playerName;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private double winPercentage;
    private double avgNumOfTurn;
    private double turnDuration;
    private int score;

    public PlayerStats(int rank, String playerName, int gamesPlayed, int gamesWon, int gamesLost, double winPercentage, double avgNumOfTurn, double turnDuration, int score) {
        this.rank = rank;
        this.playerName = playerName;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.winPercentage = winPercentage;
        this.avgNumOfTurn = avgNumOfTurn;
        this.turnDuration = turnDuration;
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public double getAvgTurnsPerGame() {
        return avgNumOfTurn;
    }

    public double getAvgTurnDuration() {
        return turnDuration;
    }


    @Override
    public String toString() {
        return "PlayerStats{" +
                "rank=" + rank +
                ", playerName='" + playerName + '\'' +
                ", gamesPlayed=" + gamesPlayed +
                ", gamesWon=" + gamesWon +
                ", gamesLost=" + gamesLost +
                ", winPercentage=" + winPercentage +
                ", avgNumOfTurn=" + avgNumOfTurn +
                ", turnDuration=" + turnDuration +
                ", score=" + score +
                '}';
    }
}
