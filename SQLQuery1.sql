--https://stackoverflow.com/questions/59641684/create-database-if-db-does-not-exist

 IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'LoginWellington')
  BEGIN
	CREATE DATABASE LoginWellington

    END
    GO
       USE LoginWellington
    GO

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='userData' and xtype='U')
BEGIN
CREATE TABLE userData 
( 
	ID INT IDENTITY(1,1) PRIMARY KEY, 
	nome VARCHAR(60) UNIQUE NOT NULL, 
	senha VARCHAR(60) NOT NULL, 
	cargo VARCHAR(20) NOT NULL,
	bloqueado BIT NOT NULL
) 
END

SELECT * FROM userData