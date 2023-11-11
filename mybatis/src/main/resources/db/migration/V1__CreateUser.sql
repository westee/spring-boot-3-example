CREATE TABLE USER (
    ID BIGINT(20) NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(255) NOT NULL,
    CREATED_AT DATETIME NOT NULL,
    UPDATED_AT DATETIME NOT NULL,
    PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8