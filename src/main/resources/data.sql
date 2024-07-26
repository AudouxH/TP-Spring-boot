INSERT INTO app_user (username, email, password, role, tokenInvalidatedAt) VALUES
('user1', 'user1@example.com', '$2a$10$7Q76eO1QV09Kj4Y2TmxtiO.x/fBdT4ixkxN1F5mkgEBlf3DxS9Juy', 'USER', NULL),
('user2', 'user2@example.com', '$2a$10$U9J1OD2.jbE1/h41wG46Ouy7kF4u1oO4Uzeh.s1os7n8rx6Kr3.JG', 'USER', NULL);

-- Inserting initial data for to_do_list
INSERT INTO toDoList (username) VALUES
('user1'),
('user2');

-- Inserting initial data for task
INSERT INTO task (checked, libelle, todoListId) VALUES
(false, 'Task 1 for User 1', 1),
(true, 'Task 2 for User 1', 1),
(false, 'Task 1 for User 2', 2);

