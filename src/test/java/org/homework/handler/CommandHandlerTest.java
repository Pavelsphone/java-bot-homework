package org.homework.handler;

import org.homework.commands.Command;
import org.homework.commands.CommandMapping;
import org.homework.services.SenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Chat;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

class CommandHandlerTest {

    @Mock
    private SenderService senderService;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    private CommandHandler commandHandler;
    private Set<Command> commands;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.getId()).thenReturn(123456789L);
        when(message.getChatId()).thenReturn(123456789L);

        commands = new HashSet<>();
        commandHandler = new CommandHandler(commands, senderService);
    }

    @Test
    void testHandleUnknownCommand() {
        when(message.isCommand()).thenReturn(true);
        when(message.getText()).thenReturn("/unknown");

        commandHandler.handle(update);

        verify(senderService).sendMessage("123456789", "Неизвестная команда. Введите /help для помощи.");
    }

    @Test
    void testHandleNonCommand() {
        when(message.isCommand()).thenReturn(false);

        commandHandler.handle(update);

        verify(senderService, never()).sendMessage(anyString(), anyString());
    }

    @Test
    void testHandleKnownCommand() {
        // Создаем реальный экземпляр команды
        Command mockCommand = mock(Command.class);

        // Создаем commandSet с мок командой
        Set<Command> commandSet = new HashSet<>();
        commandSet.add(mockCommand);

        // Создаем CommandHandler с командами
        CommandHandler handler = new CommandHandler(commandSet, senderService);

        // Настраиваем моки для сообщения с командой /test
        when(message.isCommand()).thenReturn(true);
        when(message.getText()).thenReturn("/test");

        // Добавляем команду вручную в маппинг через рефлексию
        try {
            java.lang.reflect.Field commandMapField = CommandHandler.class.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<String, Command> commandMap =
                    (java.util.Map<String, Command>) commandMapField.get(handler);
            commandMap.put("test", mockCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        handler.handle(update);
        verify(mockCommand).execute(update);
    }
}

