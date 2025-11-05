package org.homework.services;

import org.homework.bot.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.mockito.Mockito.*;

class SenderServiceTest {

    @Mock
    private Bot bot;

    private SenderService senderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        senderService = new SenderService();

        // Используем reflection для установки бота, так как поле приватное
        try {
            java.lang.reflect.Field botField = SenderService.class.getDeclaredField("bot");
            botField.setAccessible(true);
            botField.set(senderService, bot);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSendMessage() throws TelegramApiException {
        String chatId = "123456789";
        String text = "Test message";

        senderService.sendMessage(chatId, text);

        // Проверяем, что метод execute был вызван с правильным объектом SendMessage
        verify(bot).execute(any(SendMessage.class));
    }

    @Test
    void testSendMessageHandlesException() throws TelegramApiException {
        String chatId = "123456789";
        String text = "Test message";

        // Настраиваем мок, чтобы выбрасывал исключение
        doThrow(new TelegramApiException("Test exception"))
                .when(bot)
                .execute(any(SendMessage.class));

        // Метод не должен пробрасывать исключение
        senderService.sendMessage(chatId, text);
    }
}

