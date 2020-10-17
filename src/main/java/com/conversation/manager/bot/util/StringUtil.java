package com.conversation.manager.bot.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public static String replaceLastCharWithDot(String str) {
        return str.substring(0, str.length() - 1).concat(".");
    }
}
