package org.homework.bot;

import org.homework.di.annotations.Inject;
import org.homework.handler.CommandHandler;
import org.homework.logger.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

    @Inject
    private CommandHandler commandHandler;

    @Override
    public String getBotUsername() {
        return "YOUR_BOT_USERNAME";
    }

    @Override
    public String getBotToken() {
        return "YOUR_BOT_TOKEN";
    }

    @Override
    public void onUpdateReceived(Update update) {
        LoggerFactory.getLogger().debug("Received update: " + update);
        commandHandler.handle(update);
    }
}
