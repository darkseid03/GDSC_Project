CREATE DATABASE Consultations;

USE  Consultations;

CREATE TABLE Users(
SlNo INT AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(100),
Country VARCHAR(50),
ContInfo VARCHAR(100)UNIQUE,
Issue VARCHAR(255)
);

DESCRIBE Users;

SELECT * FROM Users;
