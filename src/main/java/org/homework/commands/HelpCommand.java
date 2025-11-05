package org.homework.commands;

import org.homework.di.annotations.Component;
import org.homework.services.SenderService;
import org.homework.di.annotations.Inject;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@CommandMapping("help")
public class HelpCommand implements Command {

    private final SenderService senderService;

    @Inject
    public HelpCommand(SenderService senderService) {
        this.senderService = senderService;
    }

    @Override
    public void execute(Update update) {
        senderService.sendMessage(
                update.getMessage().getChatId().toString(),
                """
                Доступные команды:
                /start — стартовое сообщение
                /help — эта справка
                """
        );
    }
}

