CREATE DATABASE Civil_Lawyers;

USE Civil_Lawyers;

CREATE TABLE Users(
SlNo INT AUTO_INCREMENT PRIMARY KEY,
Name VARCHAR(50),
ContInfo VARCHAR(100),
BarCounNo INT UNIQUE,
OffAddress VARCHAR(100),
Price INT
);

DESCRIBE Users;

SELECT * FROM Users;



