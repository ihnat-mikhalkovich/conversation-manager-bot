package com.conversation.manager.bot.util.bundle;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Locale;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

@Component
@Getter
public class ResourceBundleMessageSourceManagerImpl implements ResourceBundleMessageSourceManager {

    private ResourceBundleMessageSource bundleMessageSource;

    @Autowired
    @Qualifier("bundleMessageSource")
    public void setBundleMessageSource(ResourceBundleMessageSource bundleMessageSource) {
        this.bundleMessageSource = bundleMessageSource;
    }

    @Override
    public String findMessage(Update update, String code) {
        return this.findMessage(update, code, null);
    }

    @Override
    public String findMessage(Update update, String code, Object[] args) {
        final String languageCode = update.getMessage().getFrom().getLanguageCode();
        final Locale locale = Locale.forLanguageTag(languageCode);
        final String message = bundleMessageSource.getMessage(code, args, locale);
        return StringEscapeUtils.unescapeJava(message);
    }
}
