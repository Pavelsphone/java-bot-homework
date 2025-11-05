package org.homework.bot;

import org.homework.handler.CommandHandler;
import org.homework.logger.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Chat;

import static org.mockito.Mockito.*;

class BotTest {

    @Mock
    private CommandHandler commandHandler;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Chat chat;

    private Bot bot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bot = new Bot() {
            @Override
            public String getBotUsername() {
                return "test_bot";
            }

            @Override
            public String getBotToken() {
                return "test_token";
            }
        };

        // Устанавливаем commandHandler через reflection
        try {
            java.lang.reflect.Field handlerField = Bot.class.getDeclaredField("commandHandler");
            handlerField.setAccessible(true);
            handlerField.set(bot, commandHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testOnUpdateReceived() {
        when(update.hasMessage()).thenReturn(true);
        when(update.getMessage()).thenReturn(message);

        bot.onUpdateReceived(update);

        // Проверяем, что commandHandler.handle был вызван
        verify(commandHandler).handle(update);
    }
}



