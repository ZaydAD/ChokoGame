package JavaMVP.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard {

    private final JDBC database; // Use existing JDBC connection
    private GameSession gameSession;

    public Leaderboard(JDBC database, GameSession gameSession) {
        this.database = database;
        this.gameSession = gameSession;
    }

    public List<PlayerStats> getLeaderboardData() {
        List<PlayerStats> rankings = new ArrayList<>();
        String query = "SELECT " +
                "DENSE_RANK() OVER (ORDER BY u.total_points DESC) AS rank\n, " +
                "u.username, " +
                "(u.total_wins + u.total_losses) AS games_played, " +
                "u.total_wins, " +
                "u.total_losses, " +
                "COALESCE(ROUND((u.total_wins * 100.0) / NULLIF((u.total_wins + u.total_losses), 0), 2), 0) AS win_percentage, " +
                "COALESCE(AVG(t.turn_number), 0) AS avg_turns_per_game, " +
                "COALESCE(AVG(t.avg_turn_duration), 0) AS avg_turn_duration, " +
                "u.total_points " +
                "FROM Users u " +
                "LEFT JOIN Games g ON u.user_id = g.user_id " +
                "LEFT JOIN Turns t ON g.game_id = t.game_id " +
                "GROUP BY u.user_id, u.username, u.total_wins, u.total_losses, u.total_points " +
                "ORDER BY u.total_points DESC;";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PlayerStats player = new PlayerStats(
                        rs.getInt("rank"),
                        rs.getString("username"),
                        rs.getInt("games_played"),
                        rs.getInt("total_wins"),
                        rs.getInt("total_losses"),
                        rs.getDouble("win_percentage"),
                        rs.getDouble("avg_turns_per_game"),
                        rs.getDouble("avg_turn_duration"),
                        rs.getInt("total_points")
                );
                rankings.add(player);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankings;
    }


    public void updateWinLoss(String username) {
        String query = switch (gameSession.getGameStatus()) {
            case WON -> "UPDATE Users SET total_wins = total_wins + 1 WHERE username = ?";
            case LOST -> "UPDATE Users SET total_losses = total_losses + 1 WHERE username = ?";
            default -> null;
        };

        if (query == null) return;

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerScore(String username, int score) {
        String query = "UPDATE Users SET total_points = COALESCE(total_points, 0) + ? WHERE username = ?";
        try (Connection conn = database.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, score);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserIdByUsername(String username) {
        String query = "SELECT user_id FROM Users WHERE username = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Not found
    }

    public int insertGame(String currentPlayer, int current_color, boolean drop_initiative, long start_time, long end_time) {
        String query = "INSERT INTO Games (user_id, current_color, drop_initiative, start_at, finished_at) VALUES (?, ?, ?, ?, ?) RETURNING game_id";
        int gameId = -1;

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Retrieve user_id using username (currentPlayer)
            int userId = getUserIdByUsername(currentPlayer);
            Timestamp timestamp = new Timestamp(start_time);
            Timestamp timestamp2 = new Timestamp(end_time);

            if (userId == -1) {
                throw new SQLException("User not found for username: " + currentPlayer);
            }

            stmt.setInt(1, userId);
            stmt.setInt(2, current_color);
            stmt.setBoolean(3, drop_initiative);
            stmt.setTimestamp(4, timestamp);
            stmt.setTimestamp(5, timestamp2);

            // Execute and get the game_id
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                gameId = rs.getInt("game_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return gameId;
    }

    public void insertTurn(int gameId, int turnNumber, double avg_turn_duration) {
        String query = "INSERT INTO Turns (game_id, turn_number,avg_turn_duration) VALUES (?, ?, ?)";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, turnNumber);
            stmt.setDouble(3, avg_turn_duration);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[] getWinsAndLosses(String username) {
        int[] result = new int[2]; // [0] = wins, [1] = losses
        String query = "SELECT total_wins, total_losses FROM Users WHERE username = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result[0] = rs.getInt("total_wins");
                result[1] = rs.getInt("total_losses");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}