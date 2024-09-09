create database JAVAProjectDB
go
use JAVAProjectDB
go

create table Movie
(
IDMovie int primary key identity,
Title nvarchar(256),
[Description] nvarchar(max),
PublishedDate datetime,
PicturePath nvarchar(256),
Link nvarchar(256)
)

create table Actor
(
IDActor int primary key identity,
[Name] nvarchar(150)
)

create table Director
(
IDDirector int primary key identity,
[Name] nvarchar(150)
)

create table Genre
(
IDGenre int primary key identity,
[Name] nvarchar(150)
)

create table [User]
(
IDUser int primary key identity,
Username nvarchar(256),
[Password] nvarchar(256),
isAdmin bit
)

create table MovieActor 
(
IDMovieActor int primary key identity,
MovieID int foreign key references Movie(IDMovie),
ActorID int foreign key references Actor(IDActor)
)

create table MovieDirector
(
IDMovieDirector int primary key identity,
MovieID int foreign key references Movie(IDMovie),
DirectorID int foreign key references Director(IDDirector)
)

create table MovieGenre
(
IDMovieGenre int primary key identity,
MovieID int foreign key references Movie(IDMovie),
GenreID int foreign key references Genre(IDGenre)
)

--USER REGISTER PROCEDURE--

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE registerUser
    @username NVARCHAR(256),
    @password NVARCHAR(256),
    @IDUser INT OUTPUT
AS
BEGIN
    INSERT INTO [User] (Username, [Password], isAdmin) 
    VALUES (@username, @password, 0);

    SET @IDUser = SCOPE_IDENTITY();
END;
GO

--USER LOGIN PROCEDURE--

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE loginUser
    @username NVARCHAR(256),
    @password NVARCHAR(256)
AS
BEGIN
    IF EXISTS (SELECT 1 FROM [User] WHERE Username = @Username AND [Password] = @Password)
    BEGIN
        SELECT 1 AS LoginSuccess, IsAdmin
        FROM [User]
        WHERE Username = @Username AND [Password] = @Password;
    END
    ELSE
    BEGIN
        SELECT 0 AS LoginSuccess;
    END
END;
GO

-- USER EXIST CHECK PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE userExists
    @username NVARCHAR(256)
AS
BEGIN
    SELECT 1
    FROM [User]
    WHERE Username = @username;
END;
GO

-- SELECT USER PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectUser
    @Username NVARCHAR(255)
AS
BEGIN
    SELECT IDUser, Username, IsAdmin
    FROM [User]
    WHERE Username = @Username;
END;
GO

-- SELECT ALL USERS PROCEDURE --

USE JAVAProjectDB;
GO



-- SELECT ACTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectActor
    @IDActor INT
AS
BEGIN
    -- Select the actor with the given ID
    SELECT IDActor, [Name]
    FROM Actor
    WHERE IDActor = @IDActor;
END;

-- SELECT ALL ACTORS PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE selectActors
AS
BEGIN
    SELECT IDActor, [Name]
    FROM Actor;
END;

-- CREATE ACTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE createActor
    @Name NVARCHAR(150),
    @IDActor INT OUTPUT
AS
BEGIN
    -- Ensure uniqueness by checking if the actor name already exists
    IF EXISTS (SELECT 1 FROM Actor WHERE [Name] = @Name)
    BEGIN
        RAISERROR('An actor with the name ''%s'' already exists.', 16, 1, @Name)
        RETURN
    END

    -- Insert new actor
    INSERT INTO Actor ([Name])
    VALUES (@Name);

    -- Return the ID of the newly createed actor
    SET @IDActor = SCOPE_IDENTITY();
END;

-- UPDATE ACTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE updateActor
    @IDActor INT,
    @Name NVARCHAR(150)
AS
BEGIN
    -- Ensure the new name is unique for all actors except the current one
    IF EXISTS (SELECT 1 FROM Actor WHERE [Name] = @Name AND IDActor <> @IDActor)
    BEGIN
        RAISERROR('An actor with the name ''%s'' already exists.', 16, 1, @Name)
        RETURN
    END

    -- Update the actor's name
    UPDATE Actor
    SET [Name] = @Name
    WHERE IDActor = @IDActor;
END;

-- DELETE ACTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE deleteActor
    @IDActor INT
AS
BEGIN
    -- Delete the actor
    DELETE FROM Actor
    WHERE IDActor = @IDActor;
END;

-- FIND ACTOR BY NAME CHECK PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectActorByName
    @Name NVARCHAR(150)
AS
BEGIN
    -- Select the actor with the given name
    SELECT IDActor, [Name]
    FROM Actor
    WHERE [Name] = @Name;
END;
GO
-- CREATE DIRECTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE createDirector
    @Name NVARCHAR(150),
    @IDDirector INT OUTPUT
AS
BEGIN
    -- Ensure uniqueness by checking if the director name already exists
    IF EXISTS (SELECT 1 FROM Director WHERE [Name] = @Name)
    BEGIN
        RAISERROR('A director with the name ''%s'' already exists.', 16, 1, @Name)
        RETURN
    END

    -- Insert new director
    INSERT INTO Director ([Name])
    VALUES (@Name);

    -- Return the ID of the newly createed director
    SET @IDDirector = SCOPE_IDENTITY();
END;

-- UPDATE DIRECTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE updateDirector
    @IDDirector INT,
    @Name NVARCHAR(150)
AS
BEGIN
    -- Ensure the new name is unique for all directors except the current one
    IF EXISTS (SELECT 1 FROM Director WHERE [Name] = @Name AND IDDirector <> @IDDirector)
    BEGIN
        RAISERROR('A director with the name ''%s'' already exists.', 16, 1, @Name)
        RETURN
    END

    -- Update the director's name
    UPDATE Director
    SET [Name] = @Name
    WHERE IDDirector = @IDDirector;
END;

-- DELETE DIRECTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE deleteDirector
    @IDDirector INT
AS
BEGIN
    -- Delete the director
    DELETE FROM Director
    WHERE IDDirector = @IDDirector;
END;

-- SELECT DIRECTOR PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectDirector
    @IDDirector INT
AS
BEGIN
    -- Select the director with the given ID
    SELECT IDDirector, [Name]
    FROM Director
    WHERE IDDirector = @IDDirector;
END;

-- SELECT ALL DIRECTORS PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectDirectors
AS
BEGIN
    -- Select all directors
    SELECT IDDirector, [Name]
    FROM Director;
END;

-- FIND DIRECTOR BY NAME CHECK PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectDirectorByName
    @Name NVARCHAR(150)
AS
BEGIN
    -- Select the director with the given name
    SELECT IDDirector, [Name]
    FROM Director
    WHERE [Name] = @Name;
END;
GO

-- CREATE GENRE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE createGenre
    @Name NVARCHAR(150),
    @IDGenre INT OUTPUT
AS
BEGIN
    -- Ensure uniqueness by checking if the genre name already exists
    IF EXISTS (SELECT 1 FROM Genre WHERE [Name] = @Name)
    BEGIN
        RAISERROR('A genre with the name ''%s'' already exists.', 16, 1, @Name)
        RETURN
    END

    -- Insert new genre
    INSERT INTO Genre ([Name])
    VALUES (@Name);

    -- Return the ID of the newly createed genre
    SET @IDGenre = SCOPE_IDENTITY();
END;

-- UPDATE GENRE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE updateGenre
    @IDGenre INT,
    @Name NVARCHAR(150)
AS
BEGIN
    -- Ensure the new name is unique for all genres except the current one
    IF EXISTS (SELECT 1 FROM Genre WHERE [Name] = @Name AND IDGenre <> @IDGenre)
    BEGIN
        RAISERROR('A genre with the name ''%s'' already exists.', 16, 1, @Name)
        RETURN
    END

    -- Update the genre's name
    UPDATE Genre
    SET [Name] = @Name
    WHERE IDGenre = @IDGenre;
END;

-- DELETE GENRE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE deleteGenre
    @IDGenre INT
AS
BEGIN
    -- Delete the genre
    DELETE FROM Genre
    WHERE IDGenre = @IDGenre;
END;

-- SELECT GENRE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE selectGenre
    @IDGenre INT
AS
BEGIN
    -- Select the genre with the given ID
    SELECT IDGenre, [Name]
    FROM Genre
    WHERE IDGenre = @IDGenre;
END;

-- SELECT GENRES PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE selectGenres
AS
BEGIN
    -- Select all genres
    SELECT IDGenre, [Name]
    FROM Genre;
END;

-- FIND GENRE BY NAME CHECK PROCEDURE --

USE JAVAProjectDB;
GO

CREATE or alter PROCEDURE selectGenreByName
    @Name NVARCHAR(150)
AS
BEGIN
    -- Select the genre with the given name
    SELECT IDGenre, [Name]
    FROM Genre
    WHERE [Name] = @Name;
END;
GO
-- CREATE MOVIE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE createMovie
    @Title NVARCHAR(255),
    @Description NVARCHAR(255),
    @PublishedDate DATETIME,
    @PicturePath NVARCHAR(255),
    @Link NVARCHAR(255),
    @IDMovie INT OUTPUT
AS
BEGIN
    INSERT INTO Movie (Title, Description, PublishedDate, PicturePath, Link)
    VALUES (@Title, @Description, @PublishedDate, @PicturePath, @Link);

    SET @IDMovie = SCOPE_IDENTITY();
END;
GO

-- UPDATE MOVIE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE updateMovie
    @IDMovie INT,
    @Title NVARCHAR(255),
    @Description NVARCHAR(255),
    @PublishedDate DATETIME,
    @PicturePath NVARCHAR(255),
    @Link NVARCHAR(255)
AS
BEGIN
    UPDATE Movie
    SET Title = @Title,
        Description = @Description,
        PublishedDate = @PublishedDate,
        PicturePath = @PicturePath,
        Link = @Link
    WHERE IDMovie = @IDMovie;
END;
GO

-- DELETE MOVIE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE deleteMovie
    @IDMovie INT
AS
BEGIN
    DELETE FROM Movie WHERE IDMovie= @IDMovie;
END;
GO

-- SELECT MOVIE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE selectMovie
      @IDMovie INT
AS
BEGIN
    SELECT IDMovie, Title, Description, PublishedDate, PicturePath, Link
    FROM Movie
    WHERE IDMovie = @IDMovie;
END;
GO

-- SELECT ALL MOVIES PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE selectMovies
AS
BEGIN
    SELECT IDMovie, Title, Description, PublishedDate, PicturePath, Link
    FROM Movie;
END;
GO

-- FIND MOVIE BY TITLE CHECK PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE selectMovieByName
    @Title NVARCHAR(256)
AS
BEGIN
    SELECT IDMovie, Title, [Description], PublishedDate, PicturePath, [Link]
    FROM Movie
    WHERE Title = @Title;
END;
GO

-- SELECT MOVIE BY TITLE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE selectMovieByName
    @Title NVARCHAR(255)
AS
BEGIN
    SELECT IDMovie, Title, Description, PublishedDate, PicturePath, Link
    FROM Movie
    WHERE Title = @Title;
END;
GO

-- INSERT MOVIE ACTOR M-N --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE createMovieActor
    @MovieID INT,
    @ActorID INT
AS
BEGIN
    INSERT INTO MovieActor (MovieID, ActorID)
    VALUES (@MovieID, @ActorID);
END;
GO

-- DELETE MOVIE ACTOR M-N --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE deleteMovieActors
    @MovieID INT
AS
BEGIN
    DELETE FROM MovieActor
    WHERE MovieID = @MovieID  
END;
GO

-- SELECT ACTORS FOR MOVIE M-N --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE selectMovieActors
    @MovieID INT
AS
BEGIN
    SELECT ActorID
    FROM MovieActor
    WHERE MovieID = @MovieID;
END

-- INSERT MOVIE DIRECTOR M-N --

USE JAVAProjectDB;
GO

CREATE PROCEDURE createMovieDirector
    @MovieID INT,
    @DirectorID INT
AS
BEGIN
    INSERT INTO MovieDirector (MovieID, DirectorID)
    VALUES (@MovieID, @DirectorID);
END;
GO

-- DELETE MOVIE DIRECTOR M-N --

USE JAVAProjectDB;
GO

CREATE PROCEDURE deleteMovieDirectors
    @MovieID INT
AS
BEGIN
    DELETE FROM MovieDirector
    WHERE MovieID = @MovieID;
END;
GO

-- SELECT DIRECTORS FOR MOVIE M-N --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE selectMovieDirectors
    @MovieID INT
AS
BEGIN
    SELECT DirectorID
    FROM MovieDirector
    WHERE MovieID = @MovieID;
END;
GO

-- INSERT MOVIE GENRE M-N --

USE JAVAProjectDB;
GO

CREATE PROCEDURE createMovieGenre
    @MovieID INT,
    @GenreID INT
AS
BEGIN
    INSERT INTO MovieGenre (MovieID, GenreID)
    VALUES (@MovieID, @GenreID);
END;
GO

-- DELETE MOVIE GENRE M-N --

USE JAVAProjectDB;
GO

CREATE PROCEDURE deleteMovieGenres
    @MovieID INT
AS
BEGIN
    DELETE FROM MovieGenre
    WHERE MovieID = @MovieID;
END;
GO

-- SELECT GENRES FOR MOVIE M-N --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE selectMovieGenres
    @MovieID INT
AS
BEGIN
    SELECT GenreID
    FROM MovieGenre
    WHERE MovieID = @MovieID;
END;
GO

-- DELETE ALL MOVIE RELATIONS --

USE JAVAProjectDB;
GO

CREATE OR ALTER PROCEDURE deleteMovieRelations
    @MovieID INT
AS
BEGIN
    DELETE FROM MovieActor WHERE MovieID = @MovieID;
    DELETE FROM MovieDirector WHERE MovieID = @MovieID;
    DELETE FROM MovieGenre WHERE MovieID = @MovieID;
END;
GO

-- CLEAN DATABASE PROCEDURE --

USE JAVAProjectDB;
GO

CREATE PROCEDURE cleanDatabase
AS
BEGIN
    
    TRUNCATE TABLE MovieActor;
    TRUNCATE TABLE MovieDirector;
    TRUNCATE TABLE MovieGenre;
    
    -- Delete data from the main tables
	DELETE FROM Movie;
	DBCC CHECKIDENT ('Movie', RESEED, 0);
	DELETE FROM Actor;
	DBCC CHECKIDENT ('Actor', RESEED, 0);
	DELETE FROM Director;
	DBCC CHECKIDENT ('Director', RESEED, 0);
	DELETE FROM Genre;
	DBCC CHECKIDENT ('Genre', RESEED, 0);
END;
GO

