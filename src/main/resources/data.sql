
INSERT INTO sessions (session_name, session_desc) VALUES
('Session 1', 'Description de la session 1'),
('Session 2', 'Description de la session 2'),
('Session 3', 'Description de la session 3');

INSERT INTO speakers (first_name, last_name, title, company, speaker_bio) VALUES
('John', 'Doe', 'Software Engineer', 'TechCorp', 'John is a senior software engineer at TechCorp with over 10 years of experience.'),
('Jane', 'Smith', 'Project Manager', 'InnovateX', 'Jane has been managing projects at InnovateX for 5 years.'),
('Emily', 'Jones', 'Data Scientist', 'DataWorks', 'Emily specializes in data analysis and machine learning.');

INSERT INTO session_speakers (session_id, speaker_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 3);

INSERT INTO app_user (username, password, role) VALUES
('user1', '$2a$10$7.6C/KKlF3VJfD5yph.zsO5s9QGmU61LpT6B7bUk9f/eJ8wKhHRfW', 'USER'),
('admin', '$2a$10$7.6C/KKlF3VJfD5yph.zsO5s9QGmU61LpT6B7bUk9f/eJ8wKhHRfW', 'ADMIN');