package org.homework.handler;

import org.homework.commands.Command;
import org.homework.commands.CommandMapping;
import org.homework.di.annotations.Component;
import org.homework.di.annotations.Inject;
import org.homework.services.SenderService;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class CommandHandler {

    private final Map<String, Command> commandMap = new HashMap<>();
    private final SenderService senderService;

    @Inject
    public CommandHandler(Set<Command> commands, SenderService senderService) {
        this.senderService = senderService;
        for (Command cmd : commands) {
            if (cmd.getClass().isAnnotationPresent(CommandMapping.class)) {
                CommandMapping mapping = cmd.getClass().getAnnotation(CommandMapping.class);
                commandMap.put(mapping.value(), cmd);
            }
        }
    }

    public void handle(Update update) {
        if (update.hasMessage() && update.getMessage().isCommand()) {
            String text = update.getMessage().getText().substring(1); // убираем "/"
            Command cmd = commandMap.get(text);
            if (cmd != null) {
                cmd.execute(update);
            } else {
                senderService.sendMessage(
                        update.getMessage().getChatId().toString(),
                        "Неизвестная команда. Введите /help для помощи."
                );
            }
        }
    }
}

