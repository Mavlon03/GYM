package uz.pdp.gym.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uz.pdp.gym.repo.TgUserRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BotService {

    public static TelegramBot telegramBot = new TelegramBot("7449264666:AAF8u0tmTIVTKcQDET8G-9joMuoI2G6AIac");
    private static final TgUserRepo tgUserRepo = new TgUserRepo();
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private static final EntityManager em = emf.createEntityManager();
    public static TgUser getOrCreateUser(Long chatId) {
        // Foydalanuvchi mavjudligini tekshirib ko'ramiz
        TgUser user = getUserFromDB(chatId);

        // Agar foydalanuvchi topilmasa, yangi foydalanuvchi yaratamiz va ma'lumotlar bazasiga saqlaymiz
        if (user == null) {
            user = new TgUser();
            user.setChatId(chatId);
//            user.setPhone("+998945060801");

            // Ma'lumotlar bazasiga yozish
            saveUserToDB(user);
        }

        return user;
    }

    // Foydalanuvchini bazadan olish (JDBC yordamida)
    private static TgUser getUserFromDB(Long chatId) {
        String query = "SELECT * FROM tg_user WHERE chat_id = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, chatId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TgUser user = new TgUser();
                user.setChatId(resultSet.getLong("chat_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Foydalanuvchini ma'lumotlar bazasiga saqlash (JDBC yordamida)
    private static void saveUserToDB(TgUser user) {
        String query = "INSERT INTO tg_user (chat_id, phone) VALUES (?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getChatId());
            statement.setString(2, user.getPhone());  // Telefon raqamini qo'shish
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Start xabarini yuborish va foydalanuvchidan telefon raqamini olish
    public static void acceptStartWelcomeMessage(TgUser tgUser) {
        SendMessage sendMessage = new SendMessage(tgUser.getChatId(),
                "Assalomu aleykum\nBotimizga xush kelibsiz!\nIltimos kontaktingizni yuboring:");
        KeyboardButton keyboardButton = new KeyboardButton("Kontakt yuborish");
        keyboardButton.requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton);
        replyKeyboardMarkup.resizeKeyboard(true);

        sendMessage.replyMarkup(replyKeyboardMarkup);
        telegramBot.execute(sendMessage);
        tgUser.setTgState(TgState.MENU);  // Foydalanuvchi hali telefon raqamini yubormagan
    }

    public static void acceptContactAndChooseMenu(TgUser tgUser, Contact contact) {
        if (contact != null && contact.phoneNumber() != null) {
            String phone = PhoneNumber.fix(contact.phoneNumber());

            // Telefon raqamni foydalanuvchiga o'rnatish va saqlash
            tgUser.setPhone(phone);
            saveOrUpdateUser(tgUser);

            // Xabarlarni yuborish
            SendMessage successMessage = new SendMessage(tgUser.getChatId(), "Kontaktingiz qabul qilindi.");
            successMessage.replyMarkup(new ReplyKeyboardRemove());
            telegramBot.execute(successMessage);

            SendMessage menuMessage = new SendMessage(tgUser.getChatId(), "Tanlang:");
            menuMessage.replyMarkup(generateMenu());
            telegramBot.execute(menuMessage);

            tgUser.setTgState(TgState.MENU);
        } else {
            // Agar kontakt noto'g'ri bo'lsa
            SendMessage errorMessage = new SendMessage(tgUser.getChatId(), "Iltimos, telefon raqamingizni qayta yuboring.");
            telegramBot.execute(errorMessage);
        }
    }

    private static void saveOrUpdateUser(TgUser user) {
        String query;
        if (userExists(user.getChatId())) {
            // Agar foydalanuvchi mavjud bo'lsa, uni yangilash
            query = "UPDATE tg_user SET phone = ? WHERE chat_id = ?";
        } else {
            // Agar foydalanuvchi mavjud bo'lmasa, uni qo'shish
            query = "INSERT INTO tg_user (chat_id, phone) VALUES (?, ?)";
        }

        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getPhone());
            statement.setLong(2, user.getChatId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Foydalanuvchi mavjudligini tekshirish
    private static boolean userExists(Long chatId) {
        String query = "SELECT COUNT(*) FROM tg_user WHERE chat_id = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, chatId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private static ReplyKeyboardMarkup generateMenu() {
        KeyboardButton showHistoryButton = new KeyboardButton("Show History");
        KeyboardButton qrCodeButton = new KeyboardButton("QR Code");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(showHistoryButton, qrCodeButton);
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.oneTimeKeyboard(true);

        return replyKeyboardMarkup;
    }

    public static void close() {
        em.close();
        emf.close();
    }
}
