DROP TABLE IF EXISTS telegram_group;
DROP TABLE IF EXISTS telegram_user;
DROP TABLE IF EXISTS user_group;

CREATE TABLE telegram_user (
  id int auto_increment PRIMARY KEY,
  user_id int NOT NULL,
  hash_key VARCHAR(33) NOT NULL
);
CREATE TABLE telegram_group (
  id int auto_increment PRIMARY KEY,
  group_id bigint NOT NULL
);
CREATE TABLE user_group (
  telegram_user_id int not null,
  telegram_group_id bigint not null,
  CONSTRAINT user_group_pkey PRIMARY KEY (telegram_user_id, telegram_group_id) -- explicit pk
);
