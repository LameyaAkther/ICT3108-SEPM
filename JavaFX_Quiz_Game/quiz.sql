
CREATE DATABASE quizdb;
USE quizdb;

CREATE TABLE question (
 id INT AUTO_INCREMENT PRIMARY KEY,
 question VARCHAR(255),
 op1 VARCHAR(100),
 op2 VARCHAR(100),
 op3 VARCHAR(100),
 answer VARCHAR(100)
);

INSERT INTO question(question,op1,op2,op3,answer)
VALUES
('Java is ?', 'Language', 'OS', 'Browser', 'Language'),
('MySQL is ?', 'DB', 'IDE', 'Game', 'DB');
