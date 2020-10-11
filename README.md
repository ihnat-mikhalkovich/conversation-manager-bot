# conversation-manager-bot
Conversation manager bot.

DB:  
<code>  
drop table telegram_group;  
drop table telegram_user;

CREATE TABLE telegram_user (
  user_id serial PRIMARY KEY,
  hash_key varchar(33) NOT NULL
);

CREATE TABLE telegram_group (
  group_id bigserial PRIMARY KEY
);

CREATE TABLE user_group (
  user_id    int REFERENCES telegram_user (user_id) ON UPDATE CASCADE, 
  group_id bigint REFERENCES telegram_group (group_id) ON UPDATE CASCADE, 
  CONSTRAINT user_group_pkey PRIMARY KEY (user_id, group_id)  -- explicit pk
);

INSERT INTO
    telegram_user
VALUES
  (702696623, 'ee10c315eba2c75b403ea99136f5b48d');
  
INSERT INTO
    telegram_group
VALUES
  (-1001246979812);
  
INSERT INTO
	user_group
VALUES
	(702696623, -1001246979812);
</code>