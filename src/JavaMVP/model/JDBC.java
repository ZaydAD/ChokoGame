package JavaMVP.model;

import java.sql.*;

public class JDBC {
    private static final String URL = "jdbc:postgresql://10.134.178.38:5432/game";
    private static final String USER = "game";
    private static final String PASSWORD = "7sur7";

    public JDBC() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void addNewPlayer(Player player){
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            //==Get the highest user_id and increment by 1
            ResultSet rs = statement.executeQuery("SELECT COALESCE(MAX(user_id), 0) as MaxID FROM users;");
            //coalesce in-order to return 0 if the table is empty

            if (rs.next()) {
                player.setPlayerID(rs.getInt("MaxID") + 1); //Increment by 1
            }
            //====================Query that stores new player data inside the database==========================

            statement.executeUpdate(
                    "INSERT INTO users (user_id, username, password)" +
                            "VALUES ("+ player.getPlayerID() + ", " +
                            "'"+ player.getUsername() +"', " +
                            "'"+ player.getPassword() +"');");

            connection.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    public Player loadPlayer(String username, String password) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            // Query to search for a user by username and password
            String query = "SELECT * FROM users " +
                    "WHERE lower(username) = '" + username.toLowerCase() + "' " +
                    "AND lower(password) = '" + password.toLowerCase() + "';";

            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                int user_id = rs.getInt("user_id");
                Player player = new Player();
                player.setPlayerID(user_id);
                player.setUsername(username);
                player.setPassword(password);
                return player;
            } else {
                connection.close();
                return null;
            }

        } catch (SQLException exc) {
            exc.printStackTrace();
            return null;
        }
    }
}
