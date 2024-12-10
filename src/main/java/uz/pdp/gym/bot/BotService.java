package uz.pdp.gym.bot;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Contact;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import uz.pdp.gym.config.Admin;
import uz.pdp.gym.config.History;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.repo.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Scanner;

public class BotService {

    public static TelegramBot telegramBot = new TelegramBot("7449264666:AAF8u0tmTIVTKcQDET8G-9joMuoI2G6AIac");
    private static final TgUserRepo tgUserRepo = new TgUserRepo();
    private static  final SubscriberRepo subscriber = new SubscriberRepo();
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private static final EntityManager em = emf.createEntityManager();
    public static TgSubscribe getOrCreateUser(Long chatId) {
        TgSubscribe user = getUserFromDB(chatId);

        if (user == null) {
            user = new TgSubscribe();
            user.setChat_id(chatId);
            saveUserToDB(user);
        }
        return user;
    }

    private static TgSubscribe getUserFromDB(Long chatId) {
        String query = "SELECT * FROM TgSubscribe WHERE chat_id = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, chatId);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TgSubscribe user = new TgSubscribe();
                user.setChat_id(resultSet.getLong("chat_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void saveUserToDB(TgSubscribe user) {
        String query = "INSERT INTO TgSubscribe (chat_id, phone) VALUES (?, ?)";

        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getChat_id());
            statement.setString(2, user.getPhone());  // Telefon raqamini qo'shish
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void acceptStartWelcomeMessage(TgSubscribe tgSubscribe) {
        SendMessage sendMessage = new SendMessage(tgSubscribe.getChat_id(),
                "Assalomu aleykum\nBotimizga xush kelibsiz!\nIltimos kontaktingizni yuboring:");
        KeyboardButton keyboardButton = new KeyboardButton("Kontakt yuborish");
        keyboardButton.requestContact(true);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(keyboardButton);
        replyKeyboardMarkup.resizeKeyboard(true);

        sendMessage.replyMarkup(replyKeyboardMarkup);
        telegramBot.execute(sendMessage);
        tgSubscribe.setTgState(TgState.MENU);
    }
    public static void acceptContactAndChooseMenu(TgSubscribe tgSubscribe, Contact contact) {
        if (contact != null && contact.phoneNumber() != null) {
            String phone = PhoneNumber.fix(contact.phoneNumber());
            tgSubscribe.setPhone(phone);
            saveOrUpdateUser(tgSubscribe);

            SendMessage successMessage = new SendMessage(tgSubscribe.getChat_id(), "Kontaktingiz qabul qilindi.");
            successMessage.replyMarkup(new ReplyKeyboardRemove());
            telegramBot.execute(successMessage);

            SendMessage menuMessage = new SendMessage(tgSubscribe.getChat_id(), "Menuni tanlang:");

            menuMessage.replyMarkup(generateInlineMenu());
            telegramBot.execute(menuMessage);
            tgSubscribe.setTgState(TgState.CHOOSE);

        } else {
            SendMessage errorMessage = new SendMessage(tgSubscribe.getChat_id(), "Iltimos, telefon raqamingizni qayta yuboring.");
            telegramBot.execute(errorMessage);
        }
    }


    private static InlineKeyboardMarkup generateInlineMenu() {
        InlineKeyboardButton showHistoryButton = new InlineKeyboardButton("Show History").callbackData("SHOW_HISTORY");
        InlineKeyboardButton qrCodeButton = new InlineKeyboardButton("QR Code").callbackData("QR_CODE");

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                showHistoryButton, qrCodeButton);

        return inlineKeyboard;
    }

    private static void saveOrUpdateUser(TgSubscribe user) {
        String query;

        if (userPhoneExists(user.getPhone())) {
            query = "UPDATE TgSubscribe SET chat_id = ? WHERE phone = ?";
        } else {
            query = "INSERT INTO TgSubscribe (chat_id, phone) VALUES (?, ?)";
        }

        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getChat_id());
            statement.setString(2, user.getPhone());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean userPhoneExists(String phone) {
        String checkQuery = "SELECT COUNT(*) FROM TgSubscribe WHERE phone = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(checkQuery)) {
            statement.setString(1, phone);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Agar bitta yoki undan ko'p natija bo'lsa, telefon mavjud
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }









    public static void sendUserHistory(TgSubscribe tgSubscribe, Long adminId) {
        Admin admin = loginAdmin();
        if (admin == null || !admin.getId().equals(adminId)) {
            System.out.println("Adminlik huquqi tasdiqlanmadi yoki noto‘g‘ri ma'lumot kiritildi.");
            System.out.println("Foydalanuvchi ma'lumotlari:");
            System.out.println("Ism: " + tgSubscribe.getFirstname());
            System.out.println("Familya: " + tgSubscribe.getLastname());
            return;
        }

        History history = new History();
        history.setTgSubscribe(tgSubscribe);
        history.setAdmin(admin);
        history.setScanned_At(LocalDateTime.now());

        try {
            em.getTransaction().begin();
            em.persist(history);
            em.getTransaction().commit();
            System.out.println("Tarix muvaffaqiyatli qo‘shildi.");
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        }
    }

    private static Admin loginAdmin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Admin ismini kiriting: ");
        String firstname = scanner.nextLine();
        System.out.print("Parolni kiriting: ");
        String password = scanner.nextLine();

        try {
            String query = "SELECT a FROM Admin a WHERE a.firstname = :firstname AND a.password = :password";
            return em.createQuery(query, Admin.class)
                    .setParameter("firstname", firstname)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            System.out.println("Login ma'lumotlari noto‘g‘ri yoki admin topilmadi.");
            return null;
        }
    }


    public static void sendQRCodeForUser(TgSubscribe tgSubscribe, Long chatId) {
        try {
            String qrData = "http:192.168.35.183/user:" + tgSubscribe.getChat_id();  // QR kodda foydalanuvchi haqida ma'lumot saqlanadi
            byte[] qrImage = generateQRCode(qrData);

            SendPhoto sendPhoto = new SendPhoto(tgSubscribe.getChat_id(), qrImage);
            telegramBot.execute(sendPhoto);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
    private static byte[] generateQRCode(String data) throws WriterException, IOException {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.MARGIN, 1);

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        return baos.toByteArray();
    }

    public static void close() {
        em.close();
        emf.close();
    }}
