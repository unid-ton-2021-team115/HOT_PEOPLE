-- place Table Create SQL
CREATE TABLE place
(
    `id`               VARCHAR(45)    NOT NULL, 
    `name`             VARCHAR(45)    NULL, 
    `address`          TEXT           NULL, 
    `latitude`         DOUBLE         NULL, 
    `longitude`        DOUBLE         NULL, 
    `phone_number`     TEXT           NULL, 
    `is_hot`           TINYINT        NULL, 
    `recent_post_cnt`  INT            NULL, 
     PRIMARY KEY (id)
);


-- user_kakao Table Create SQL
CREATE TABLE user_kakao
(
    `id`           INT            NOT NULL, 
    `nickname`     VARCHAR(45)    NULL, 
    `profile_url`  TEXT           NULL, 
    `gender`       VARCHAR(45)    NULL, 
    `age`          INT            NULL, 
    `age_range`    VARCHAR(45)    NULL, 
    `likes`        INT            NULL, 
     PRIMARY KEY (id)
);


-- matching Table Create SQL
CREATE TABLE matching
(
    `id`                 INT            NOT NULL    AUTO_INCREMENT, 
    `host_id`            INT            NULL, 
    `place_id`           VARCHAR(45)    NULL, 
    `create_datetime`    DATETIME       NULL, 
    `matching_datetime`  DATETIME       NULL, 
    `description`        TEXT           NULL, 
    `status`             VARCHAR(45)    NULL, 
     PRIMARY KEY (id)
);

ALTER TABLE matching
    ADD CONSTRAINT FK_matching_host_id_user_kakao_id FOREIGN KEY (host_id)
        REFERENCES user_kakao (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE matching
    ADD CONSTRAINT FK_matching_place_id_place_id FOREIGN KEY (place_id)
        REFERENCES place (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- matching_join Table Create SQL
CREATE TABLE matching_join
(
    `id`           INT            NOT NULL    AUTO_INCREMENT, 
    `matching_id`  INT            NULL, 
    `guest_id`     INT            NULL, 
    `status`       VARCHAR(45)    NULL, 
     PRIMARY KEY (id)
);

ALTER TABLE matching_join
    ADD CONSTRAINT FK_matching_join_matching_id_matching_id FOREIGN KEY (matching_id)
        REFERENCES matching (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

ALTER TABLE matching_join
    ADD CONSTRAINT FK_matching_join_guest_id_user_kakao_id FOREIGN KEY (guest_id)
        REFERENCES user_kakao (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


-- place_types Table Create SQL
CREATE TABLE place_types
(
    `id`        INT            NOT NULL    AUTO_INCREMENT, 
    `place_id`  VARCHAR(45)    NULL, 
    `type`      VARCHAR(45)    NULL, 
     PRIMARY KEY (id)
);

ALTER TABLE place_types
    ADD CONSTRAINT FK_place_types_place_id_place_id FOREIGN KEY (place_id)
        REFERENCES place (id) ON DELETE RESTRICT ON UPDATE RESTRICT;













set FOREIGN_KEY_CHECKS = 0;

insert into matching value (null, 1978095294, 'ChIJkUlNKsejfDUR7CwW83dt_WE', "2021-11-06T01:58:21", "2021-11-10T15:10:00", "같이 가실 20대 구합니다~",  "wait");
insert into matching_join value (null, 1, 1979121632, "wait");

insert into matching value (null, 1979121632, 'ChIJkUlNKsejfDUR7CwW83dt_WE', "2021-11-05T21:12:21", "2021-11-20T13:30:00", "같이 노실분",  "makeup");
insert into matching_join value (null, 2, 1978095294, "makeup");

insert into matching value (null, 1978095294, 'ChIJLTIQgMSjfDURXwcF6wtPgfo', "2021-11-01T12:12:26", "2021-11-03T15:10:00", "맘에 안든다 싶으면 마감해요",  "cancel");
insert into matching_join value (null, 3, 1979121632, "cancel");

set FOREIGN_KEY_CHECKS = 1;