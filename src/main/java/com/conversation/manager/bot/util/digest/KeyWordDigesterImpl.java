package com.conversation.manager.bot.util.digest;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class KeyWordDigesterImpl implements KeyWordDigester {

    public String digest(String word) {
        return DigestUtils.md5DigestAsHex(word.getBytes());
    }
}
