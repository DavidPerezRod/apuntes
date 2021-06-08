SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS psd2 CHARACTER SET utf8;

CREATE TABLE psd2.Token (
                         id bigint(20) NOT NULL,
                         accessToken text NOT NULL,
                         tokenType varchar(20) NOT NULL,
                         refreshToken text,
                         expiresIn int(11) NOT NULL,
                         createDate datetime NOT NULL,
                         psuId varchar(25) DEFAULT NULL,
                         refreshDate datetime DEFAULT NULL,
                         apiName varchar(25) NOT NULL,
                         revokedDate datetime DEFAULT NULL,
                         services varchar(12) DEFAULT 'AIS'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE psd2.Token
    ADD PRIMARY KEY (id);


ALTER TABLE psd2.Token
    MODIFY id bigint(20) NOT NULL AUTO_INCREMENT;
