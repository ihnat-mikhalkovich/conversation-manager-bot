DROP TABLE IF EXISTS telegram_group;
DROP TABLE IF EXISTS telegram_user;
DROP TABLE IF EXISTS user_group;

CREATE TABLE telegram_user (
  user_id int PRIMARY KEY,
  hash_key VARCHAR(33) NOT NULL
);
CREATE TABLE telegram_group (
  group_id bigint PRIMARY KEY
);
CREATE TABLE user_group (
  user_id int not null,
  group_id bigint not null,
  CONSTRAINT user_group_pkey PRIMARY KEY (user_id, group_id) -- explicit pk
);
