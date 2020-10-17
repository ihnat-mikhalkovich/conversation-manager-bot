package com.conversation.manager.bot.telegram.command.impl;

import com.conversation.manager.bot.telegram.command.AbstractBotCommand;
import com.conversation.manager.bot.telegram.command.BotCommandType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
public class GroupMigrateToSupergroupCommand extends AbstractBotCommand {

    private BotAddedToGroupCommand botAddedToGroupCommand;

    @Autowired
    public void setBotAddedToGroupCommand(BotAddedToGroupCommand botAddedToGroupCommand) {
        this.botAddedToGroupCommand = botAddedToGroupCommand;
    }

    @Override
    protected BotApiMethod<?> process(Long chatId, Update update) {
        final Long migrateToChatId = update.getMessage().getMigrateToChatId();
        return botAddedToGroupCommand.process(migrateToChatId, update);
    }

    @Override
    public BotCommandType getType() {
        return BotCommandType.GROUP_MIGRATE_TO_SUPERGROUP;
    }
}
