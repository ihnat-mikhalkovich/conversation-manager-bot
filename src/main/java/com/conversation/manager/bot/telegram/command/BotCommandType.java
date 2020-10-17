package com.conversation.manager.bot.telegram.command;

public enum BotCommandType {
    START,
    HELP,
    REMOVE,
    INVITE,
    UNRECOGNIZED,
    NO_COMMAND,
    BOT_ADDED_TO_GROUP,
    GROUP_MIGRATE_TO_SUPERGROUP,
    GROUP_CREATED_WITH_BOT,
    DO_NOTHING
}
