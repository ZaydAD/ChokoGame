-- Insert Users
INSERT INTO Users (username, password, total_wins, total_losses, total_points) VALUES
                                                                                   ('Alexander Sorodoc', 'password123', 5, 2, 450),
                                                                                   ('Calin Vasilica', 'securepass', 3, 4, 150),
                                                                                   ('LaTavia Van Dams', 'letmein', 2, 1, 175),
                                                                                   ('Zayd Ait Daoud', 'mypassword', 2, 5, 125);

-- Insert Games
INSERT INTO Games (user_id, current_color, drop_initiative, start_at, finished_at) VALUES
                                                                                       (1, 1, TRUE, '2025-02-20 10:00:00', NULL),
                                                                                       (2, 2, FALSE, '2025-02-19 15:30:00', '2025-02-19 16:00:00'),
                                                                                       (3, 1, TRUE, '2025-02-18 12:45:00', '2025-02-18 13:30:00'),
                                                                                       (4, 2, FALSE, '2025-02-17 09:15:00', NULL);

-- Insert Turns
INSERT INTO Turns (game_id, turn_number, avg_turn_duration) VALUES
                                                                (1, 10, 3.2),
                                                                (1, 11, NULL),
                                                                (2, 8, 2.9),
                                                                (2, 7, 3.7),
                                                                (3, 9, 3.1),
                                                                (3, 14, 3.6),
                                                                (4, 12, 3.3);
