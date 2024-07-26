-- Table for User entity
CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255) DEFAULT 'USER',
    tokenInvalidatedAt TIMESTAMP
);


-- Table for ToDoList entity
CREATE TABLE IF NOT EXISTS toDoList (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    CONSTRAINT fkUser FOREIGN KEY (username) REFERENCES app_user(username)
);

-- Table for Task entity
CREATE TABLE IF NOT EXISTS task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    checked BOOLEAN NOT NULL,
    libelle VARCHAR(255) NOT NULL,
    todoListId BIGINT NOT NULL,
    CONSTRAINT fkTodolist FOREIGN KEY (todoListId) REFERENCES toDoList(id) ON DELETE CASCADE
);

