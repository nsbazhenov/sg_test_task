DROP TABLE IF EXISTS CLAN;
DROP TABLE IF EXISTS PLAYER;
DROP TABLE IF EXISTS AUDIT_EVENTS;

CREATE TABLE PLAYER
(
    ID                      LONG AUTO_INCREMENT PRIMARY KEY,
    NAME                    VARCHAR(255),
    LOGIN                   VARCHAR(255),
    PASSWORD                VARCHAR(255),
    GOLD                    LONG
);

CREATE TABLE CLAN
(
    ID                      LONG AUTO_INCREMENT PRIMARY KEY,
    NAME                    VARCHAR(255),
    GOLD                    LONG
);

CREATE TABLE AUDIT_EVENTS
(
    ID                      LONG AUTO_INCREMENT PRIMARY KEY,
    EVENT_TYPE              VARCHAR(255),
    ID_CLAN                 LONG,
    ID_PLAYER               LONG,
    RESULT_OPERATION        BOOLEAN,
    DESCRIPTION_OPERATION   VARCHAR(255)
);

INSERT INTO CLAN (NAME, GOLD) VALUES ('clan', 160000);

INSERT INTO PLAYER (NAME, LOGIN, GOLD) VALUES ('player', 'player', 160000);