package org.homework.commands;

import org.homework.di.annotations.Component;
import org.homework.services.SenderService;
import org.homework.di.annotations.Inject;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@CommandMapping("start")
public class StartCommand implements Command {

    private final SenderService senderService;

    @Inject
    public StartCommand(SenderService senderService) {
        this.senderService = senderService;
    }

    @Override
    public void execute(Update update) {
        senderService.sendMessage(
                update.getMessage().getChatId().toString(),
                "Привет! Я ваш учебный бот. Введите /help для списка команд."
        );
    }
}

