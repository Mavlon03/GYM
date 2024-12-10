package uz.pdp.gym.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

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

    }
    private void handleMessage(Message message) {
        Long chatId = message.chat().id();
        TgUser tgUser = getOrCreateUser(chatId);

        if (message.text() != null && message.text().equals("/start")) {
            acceptStartWelcomeMessage(tgUser);
        } else if (message.contact() != null) {
            acceptContactAndChooseMenu(tgUser, message.contact());
        } else {
            // Kontakt yuborishni so'rash
            telegramBot.execute(new SendMessage(chatId, "Iltimos, telefon raqamingizni yuboring."));
            ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(
                    new KeyboardButton("Telefon raqamni yuborish").requestContact(true)
            );
            telegramBot.execute(new SendMessage(chatId, "Kontaktni yuborish uchun tugmani bosing").replyMarkup(keyboard));
        }
    }

}



