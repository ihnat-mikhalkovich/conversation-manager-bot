package com.conversation.manager.bot.telegram.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class BotCommandDirector {

    private final Map<BotCommandType, BotCommand> botCommandMap;

    public BotCommandDirector() {
        this.botCommandMap = new EnumMap<>(BotCommandType.class);
    }

    public void register(BotCommand botCommand) {
        botCommandMap.put(botCommand.getType(), botCommand);
    }

    public BotCommand getCommand(BotCommandType type) {
        return botCommandMap.get(type);
    }
}
