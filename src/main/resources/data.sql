INSERT INTO users (username, password, email, name, lastname, role, countLogin, lastLoginSuccess)
VALUES
('admin', '$2a$10$yRjdD/8HrUnJw5sPZfWyaOcQFNW8H8oXJ21cLsd/2ItMIxQlYEuBO', 'admin@example.com', 'Admin', 'User', 'admin', 0, NULL),
('user1', '$2a$10$yRjdD/8HrUnJw5sPZfWyaOcQFNW8H8oXJ21cLsd/2ItMIxQlYEuBO', 'user1@example.com', 'John', 'Doe', 'user', 0, NULL),
('user2', '$2a$10$yRjdD/8HrUnJw5sPZfWyaOcQFNW8H8oXJ21cLsd/2ItMIxQlYEuBO', 'user2@example.com', 'Jane', 'Doe', 'user', 0, NULL);
