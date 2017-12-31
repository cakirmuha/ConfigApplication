PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE AppConfig(
ID integer primary key not null,
Name varchar(30) not null,
Type varchar(10) not null,
Value not null,
IsActive tinyint default 0 not null,
ApplicationName varchar(30) not null);
INSERT INTO AppConfig VALUES(1,'SiteName','String','trendyol.com',1,'SERVICE-A');
INSERT INTO AppConfig VALUES(2,'IsBasketEnabled','Boolean',1,1,'SERVICE-B');
INSERT INTO AppConfig VALUES(3,'MaxItemCount','Int',128,1,'SERVICE-A');
CREATE INDEX AppActiveCongig on AppConfig(IsActive, ApplicationName);
COMMIT;
