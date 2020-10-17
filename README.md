# conversation-manager-bot
Conversation manager bot.

DB:  
<code>  
CREATE TABLE telegram_user (
   id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   user_id int NOT NULL,
   hash_key varchar(33) NOT NULL
 );

 CREATE TABLE telegram_group (
   id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
   group_id bigint NOT NULL
 );

 CREATE TABLE user_group (
   telegram_user_id    int REFERENCES telegram_user (id) ON UPDATE CASCADE, 
   telegram_group_id bigint REFERENCES telegram_group (id) ON UPDATE CASCADE, 
   CONSTRAINT user_group_pkey PRIMARY KEY (telegram_user_id, telegram_group_id)  -- explicit pk
 );

INSERT INTO
    telegram_user
VALUES
  (1, 702696623, 'ee10c315eba2c75b403ea99136f5b48d');
  
INSERT INTO
    telegram_group
VALUES
  (1, -1001246979812);
  
INSERT INTO
	user_group
VALUES
	(1, 1);
</code>