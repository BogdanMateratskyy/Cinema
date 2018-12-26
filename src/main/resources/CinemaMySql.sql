USE sys;

DROP DATABASE IF EXISTS `Cinema`;

CREATE DATABASE `Cinema`;

USE `Cinema`;

CREATE TABLE Movie (
	`Id` int auto_increment PRIMARY KEY NOT NULL,
	`Name` nvarchar(100) NOT NULL,
	`Genre` nvarchar(50) NULL
);

CREATE TABLE Schedule (
	`Id` int auto_increment PRIMARY KEY NOT NULL,
	`Time` datetime NOT NULL,
	`MovieId` int NOT NULL,
	`Price` double(10, 2) NULL,
	`IdHall` int NOT NULL,
	CONSTRAINT FK_Schedule_Movie_MovieId
		FOREIGN KEY (MovieId) REFERENCES Movie (Id)
);
