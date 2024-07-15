CREATE TABLE IF NOT EXISTS `user` (
    `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `uid` varchar(20) NOT NULL,
    `upw` varchar(100) NOT NULL,
    `nm` varchar(100) NOT NULL,
    `pic` varchar(50) DEFAULT NULL,
    `created_at` datetime NOT NULL DEFAULT current_timestamp(),
    `updated_at` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uid` (`uid`)
) ;
TRUNCATE TABLE user ;


CREATE TABLE IF NOT EXISTS `user_follow` (
    `from_user_id` bigint(20) NOT NULL,
    `to_user_id` bigint(20) NOT NULL,
    `created_at` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`from_user_id`,`to_user_id`)
) ;
TRUNCATE TABLE user_follow ;


CREATE TABLE IF NOT EXISTS `feed` (
    `feed_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `writer_id` bigint(20) NOT NULL,
    `contents` varchar(1000) DEFAULT NULL,
    `location` varchar(30) DEFAULT NULL,
    `created_at` datetime NOT NULL DEFAULT current_timestamp(),
    `updated_at` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY (`feed_id`)
) ;
TRUNCATE TABLE feed ;


CREATE TABLE IF NOT EXISTS `feed_comment` (
    `feed_comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `feed_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    `COMMENT` varchar(1000) NOT NULL,
    `created_at` datetime NOT NULL DEFAULT current_timestamp(),
    `updated_at` datetime DEFAULT NULL ON UPDATE current_timestamp(),
    PRIMARY KEY(`feed_comment_id`)
) ;
TRUNCATE TABLE feed_comment ;


CREATE TABLE IF NOT EXISTS `feed_favorite` (
    `feed_id` bigint(20) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    `created_at` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`feed_id`,`user_id`)
) ;
TRUNCATE TABLE feed_favorite ;


CREATE TABLE IF NOT EXISTS `feed_pics` (
    `feed_pic_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `pic` varchar(70) NOT NULL,
    `feed_id` bigint(20) NOT NULL,
    `created_at` datetime NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY (`feed_pic_id`)
) ;
TRUNCATE TABLE feed_pics ;

-- DML, DCL, DDL

DROP TABLE user_roles;
CREATE TABLE user_roles(
    user_id BIGINT NOT NULL
    , role_cd INT NOT NULL -- 코드값
    , role VARCHAR(20) -- 코드값과 연결된 문자열
    , PRIMARY KEY (user_id, role_cd)
);

DROP TABLE main_code;
CREATE TABLE main_code(
    main_code_id INT PRIMARY KEY auto_increment
    , cd_name VARCHAR(20) NOT NULL-- role_cd 이러한 코드의 코드
    , description VARCHAR(30) -- 코드에 대한 설명
);
DROP TABLE sub_code;
CREATE TABLE sub_code(
    sub_code_id INT PRIMARY KEY auto_increment
    , main_code_id INT NOT NULL
    , val VARCHAR(30) NOT NULL-- 코드의 문자열
    , description VARCHAR(30)
    , FOREIGN KEY (main_code_id) REFERENCES main_code(main_code_id)
);