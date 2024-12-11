package uz.pdp.gym.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.gym.config.TgSubscribe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static uz.pdp.gym.bot.BotService.*;

public class BotController {
    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        BotService.telegramBot.setUpdatesListener(updates -> {

            for (Update update : updates) {
                executorService.execute(() -> {
                    try {
                        if (update.message() != null || update.callbackQuery() != null) {
                            handleUpdates(update);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdates(Update update) {
        if (update.message() != null) {
            handleMessage(update.message());
        } else if (update.callbackQuery() != null) {
            handleCallbackQuery(update.callbackQuery());
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.data();
        Long chatId = callbackQuery.message().chat().id();
        TgSubscribe tgSubscribe = getOrCreateUser(chatId);
        if ("SHOW_HISTORY".equals(callbackData)) {
            System.out.println(tgSubscribe.getId());
            System.out.println("SHOW HISTORY ");
            BotService.sendHistoryForUser(tgSubscribe, chatId);
        } else if ("QR_CODE".equals(callbackData)) {
            System.out.println("QR code");
            BotService.sendQRCodeForUser(tgSubscribe, chatId);
        } else {
            telegramBot.execute(new SendMessage(chatId, "Incorrect order!"));
        }
    }

    private void handleMessage(Message message) {
        Long chatId = message.chat().id();
        TgSubscribe tgSubscribe = getOrCreateUser(chatId);

        if (message.text() != null && message.text().equals("/start")) {
            acceptStartWelcomeMessage(tgSubscribe);
        } else if (message.contact() != null) {
            acceptContactAndChooseMenu(tgSubscribe, message.contact());
            System.out.println(message.text());
        }
    }

}



