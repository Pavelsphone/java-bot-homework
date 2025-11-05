package org.homework.commands;

import org.homework.services.SenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Chat;

import static org.mockito.Mockito.*;

class StartCommandTest {

    @Mock
    private SenderService senderService;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    private StartCommand startCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startCommand = new StartCommand(senderService);

        when(update.getMessage()).thenReturn(message);
        when(message.getChat()).thenReturn(chat);
        when(chat.getId()).thenReturn(123456789L);
        when(message.getChatId()).thenReturn(123456789L);
    }

    @Test
    void testExecute() {
        startCommand.execute(update);

        verify(senderService).sendMessage(
                "123456789",
                "Привет! Я ваш учебный бот. Введите /help для списка команд."
        );
    }
}

