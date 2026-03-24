-- Users table
CREATE TABLE Users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       total_wins INT DEFAULT 0,
                       total_losses INT DEFAULT 0,
                       total_points INT DEFAULT 0
);

-- Games table
CREATE TABLE Games (
                       game_id SERIAL PRIMARY KEY,
                       user_id INT NOT NULL,
                       current_color INT NOT NULL,
                       drop_initiative BOOLEAN NOT NULL,
                       start_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       finished_at TIMESTAMP NULL,
                       FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- Turns table
CREATE TABLE Turns (
                       turn_id SERIAL PRIMARY KEY,
                       game_id INT NOT NULL,
                       turn_number INT NOT NULL,
                       avg_turn_duration DOUBLE PRECISION NOT NULL,
                       FOREIGN KEY (game_id) REFERENCES Games(game_id)
);
