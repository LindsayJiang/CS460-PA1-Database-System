DROP TABLE Users CASCADE;
DROP SEQUENCE Pictures_picture_id_seq CASCADE;
DROP SEQUENCE Users_user_id_seq CASCADE;
DROP SEQUENCE Comments_cid_seq CASCADE;
DROP SEQUENCE Albums_aid_seq CASCADE;
DROP TABLE Friends CASCADE;
DROP TABLE Albums CASCADE;
DROP TABLE Pictures CASCADE;
DROP TABLE Comments CASCADE;
DROP TABLE Tags;
DROP TABLE Associate CASCADE;
DROP TABLE Likes CASCADE;



CREATE SEQUENCE Pictures_picture_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 14
  CACHE 1;



CREATE SEQUENCE Users_user_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 14
  CACHE 1;


CREATE TABLE Users
(
  user_id int4 NOT NULL DEFAULT nextval('Users_user_id_seq'),
  first_name	VARCHAR(255) NOT NULL,
  last_name	VARCHAR(255) NOT NULL,
  email varchar(255) NOT NULL,
  date_of_birth	DATE,
  hometown	VARCHAR(255),
  gender		VARCHAR(255),
  password varchar(255) NOT NULL,
  role_name varchar(255) NOT NULL DEFAULT 'RegisteredUser',
  score INTEGER NOT NULL DEFAULT 0,
  CONSTRAINT users_pk PRIMARY KEY (user_id),
  CONSTRAINT users_unique_email UNIQUE (email)
);

INSERT INTO Users (first_name, last_name, email, password, score, gender) VALUES ('test', 'test', 'test@bu.edu', 'test', 0, 'male');

CREATE TABLE Friends (
	fid		INTEGER,
	user_id		INTEGER NOT NULL,
PRIMARY KEY (fid, user_id),
FOREIGN KEY(fid) REFERENCES Users(user_id) ON DELETE CASCADE);

CREATE SEQUENCE Albums_aid_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 14
  CACHE 1;

CREATE TABLE Albums (
	aid		INTEGER NOT NULL DEFAULT nextval('Albums_aid_seq'),
	user_id		INTEGER NOT NULL,
	name		VARCHAR(255),
	date_of_creation	DATE,
	PRIMARY KEY (aid),
	FOREIGN KEY(user_id) REFERENCES Users(user_id) ON DELETE CASCADE);

CREATE TABLE Pictures
(
  picture_id int4 NOT NULL DEFAULT nextval('Pictures_picture_id_seq'),
  aid		INTEGER NOT NULL,
  caption varchar(255) NOT NULL,
  imgdata bytea NOT NULL,
  size int4 NOT NULL,
  content_type varchar(255) NOT NULL,
  thumbdata bytea NOT NULL,
  FOREIGN KEY (aid) REFERENCES Albums(aid) ON DELETE CASCADE,
  CONSTRAINT pictures_pk PRIMARY KEY (picture_id)
);
CREATE SEQUENCE Comments_cid_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 14
  CACHE 1;

CREATE TABLE Comments (
	cid		INTEGER NOT NULL DEFAULT nextval('Comments_cid_seq'),
	user_id		INTEGER,
	picture_id		INTEGER NOT NULL,
	text		VARCHAR(500),
	date		DATE,
	PRIMARY KEY (cid),
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (picture_id) REFERENCES Pictures(picture_id) ON DELETE CASCADE);

CREATE TABLE Tags (
	word		VARCHAR(255),
	PRIMARY KEY (word));
CREATE TABLE Associate (
	word		VARCHAR(255),
	picture_id		INTEGER,
	PRIMARY KEY (word, picture_id),
	FOREIGN KEY (picture_id) REFERENCES Pictures(picture_id) ON DELETE CASCADE);

CREATE TABLE Likes (
	user_id		INTEGER,
	picture_id		INTEGER,
	PRIMARY KEY (user_id, picture_id),
	FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
	FOREIGN KEY (picture_id) REFERENCES Pictures(picture_id) ON DELETE CASCADE);

CREATE ASSERTION NoCommentOwn
	CHECK (NOT EXISTS (
		SELECT *
		FROM Comments C
		WHERE C.user_id IN (
				SELECT DISTINCT A.user_id
				FROM Pictures P, Albums A
				WHERE C.picture_id = P.picture_id AND
					    P.aid = A.aid)));
