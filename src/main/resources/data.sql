INSERT INTO
    telegram_user (user_id, hash_key)
VALUES
  (702696623, 'ee10c315eba2c75b403ea99136f5b48d'),
  (1101, 'd41d8cd98f00b204e9800998ecf8427e'),
  (1102, '5f4dcc3b5aa765d61d8327deb882cf99');

INSERT INTO
    telegram_group (group_id)
VALUES
  (-1001246979812),
  (201),
  (202);

INSERT into
    user_group (user_id, group_id)
values
    (702696623, -1001246979812),
    (1101, 201),
    (1102, 201),
    (1102, 202);