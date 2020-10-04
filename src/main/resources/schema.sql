DROP TABLE IF EXISTS TELEGRAM_CHAT;
DROP TABLE IF EXISTS TELEGRAM_USER;

CREATE TABLE TELEGRAM_USER (
  id IDENTITY  PRIMARY KEY,
  telegram_username VARCHAR(32) NOT NULL,
  hashed_key VARCHAR(33) NOT NULL
);

CREATE TABLE TELEGRAM_CHAT (
  id IDENTITY PRIMARY KEY,
  user_id BIGINT NOT NULL,
  chat_id BIGINT NOT NULL,
  CONSTRAINT chats_fk_1 FOREIGN KEY (user_id) REFERENCES TELEGRAM_USER (id)
);