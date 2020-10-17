INSERT INTO
    telegram_user (id, user_id, hash_key)
VALUES
  (1, 702696623, 'ee10c315eba2c75b403ea99136f5b48d'),
  (2, 1101, 'd41d8cd98f00b204e9800998ecf8427e'),
  (3, 1102, '5f4dcc3b5aa765d61d8327deb882cf99');

INSERT INTO
    telegram_group (id, group_id)
VALUES
  (1, -1001246979812),
  (2, 201),
  (3, 202);

INSERT into
    user_group (telegram_user_id, telegram_group_id)
values
    (1, 1),
    (2, 2),
    (3, 2),
    (3, 3);