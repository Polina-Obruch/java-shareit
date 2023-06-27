CREATE TYPE IF NOT EXISTS BOOKING_STATUS AS ENUM ('WAITING', 'APPROVED', 'REJECTED', 'CANCELED');

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID BIGINT AUTO_INCREMENT,
    EMAIL   VARCHAR(50) UNIQUE NOT NULL,
    NAME    VARCHAR(50)        NOT NULL,
    constraint USERS_PK
        primary key (USER_ID)
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    REQUEST_ID  BIGINT AUTO_INCREMENT PRIMARY KEY,
    DESCRIPTION VARCHAR(255) NOT NULL,
    CREATED    TIMESTAMP    NOT NULL,
    USER_ID    INTEGER      NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ITEM_ID     BIGINT AUTO_INCREMENT,
    NAME        VARCHAR(50)  NOT NULL,
    DESCRIPTION VARCHAR(255) NOT NULL,
    AVAILABLE   BOOLEAN      NOT NULL,
    OWNER_ID    INTEGER      NOT NULL,
    REQUEST_ID  INTEGER,
    constraint ITEMS_PK
        primary key (ITEM_ID),
    constraint "ITEMS_USERS_fk"
        foreign key (OWNER_ID) references USERS (USER_ID) ON DELETE CASCADE,
    constraint "ITEMS_REQUESTS_fk"
        foreign key (REQUEST_ID) REFERENCES REQUESTS (REQUEST_ID)
);

CREATE TABLE IF NOT EXISTS BOOKING
(
    BOOKING_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    START_TIME TIMESTAMP NOT NULL,
    END_TIME   TIMESTAMP NOT NULL,
    ITEM_ID    INTEGER   NOT NULL,
    BOOKER_ID  INTEGER   NOT NULL,
    STATUS     BOOKING_STATUS DEFAULT 'WAITING',
    constraint BOOKING_PK
        primary key (BOOKING_ID),
    constraint "BOOKING_USERS_fk"
        foreign key (BOOKER_ID) references USERS (USER_ID) ON DELETE CASCADE,
    constraint "BOOKING_ITEMS_fk"
        foreign key (ITEM_ID) references ITEMS ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    COMMENT_ID BIGINT AUTO_INCREMENT,
    TEXT      VARCHAR(255) NOT NULL,
    ITEM_ID    INTEGER      NOT NULL,
    AUTHOR_ID  INTEGER      NOT NULL,
    CREATED    TIMESTAMP    NOT NULL,
    constraint COMMENTS_PK
        primary key (COMMENT_ID),
    constraint "COMMENTS_ITEMS_fk"
        foreign key (ITEM_ID) references ITEMS ON DELETE CASCADE,
    constraint "COMMENTS_USERS_fk"
        foreign key (AUTHOR_ID) references USERS (USER_ID) ON DELETE CASCADE
);