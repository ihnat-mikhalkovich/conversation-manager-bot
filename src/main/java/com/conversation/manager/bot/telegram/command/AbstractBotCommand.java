package com.conversation.manager.bot.telegram.command;

import com.conversation.manager.bot.repository.GroupRepository;
import com.conversation.manager.bot.repository.UserRepository;
import com.conversation.manager.bot.service.prepare.PreparedRequestService;
import com.conversation.manager.bot.util.bundle.ResourceBundleMessageSourceManager;
import com.conversation.manager.bot.util.key.KeyPartRecognizer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
public abstract class AbstractBotCommand implements BotCommand {

    protected GroupRepository groupRepository;

    protected UserRepository userRepository;

    protected KeyPartRecognizer keyPartRecognizer;

    protected PreparedRequestService preparedRequestService;

    protected ResourceBundleMessageSourceManager bundleMessageSourceManager;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Autowired
    public void setKeyPartRecognizer(KeyPartRecognizer keyPartRecognizer) {
        this.keyPartRecognizer = keyPartRecognizer;
    }

    @Autowired
    public void setPreparedRequestService(PreparedRequestService preparedRequestService) {
        this.preparedRequestService = preparedRequestService;
    }

    @Autowired
    public void setBundleMessageSourceManager(ResourceBundleMessageSourceManager bundleMessageSourceManager) {
        this.bundleMessageSourceManager = bundleMessageSourceManager;
    }

    @Override
    public BotApiMethod<?> execute(Update update) {
        final Long chatId;
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = -1L;
        }
        return process(chatId, update);
    }

    abstract protected BotApiMethod<?> process(Long chatId, Update update);
}
