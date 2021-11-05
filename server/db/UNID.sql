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

--truncate access_record;

insert into place value ('ChIJBZMvC96hfDURYCUD0j-qQIA', '준호네돈가스', '대한민국 서울특별시 동작구 흑석동 190-37', 37.507487, 126.9592763, '02-825-8954', 1, 8); 
insert into place value ('ChIJExJA9d2hfDUR8rFDuETY_pU', '터방내', '대한민국 서울특별시 동작구 흑석동 184-19', 37.5080055, 126.960744, '02-813-4434', 1, 10); 


set FOREIGN_KEY_CHECKS = 1;

