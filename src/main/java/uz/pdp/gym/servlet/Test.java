package uz.pdp.gym.servlet;

import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.gym.bot.BotService;
import uz.pdp.gym.config.History;
import uz.pdp.gym.config.Roles;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.repo.HistoryRepo;
import uz.pdp.gym.repo.SubscriberRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@WebServlet("/scan/qrcode")
public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("chatId");
        PrintWriter writer = resp.getWriter();

        HttpSession session = req.getSession();
        Long sessionChatId = (Long) session.getAttribute("chatId");

        if (id == null || id.isEmpty()) {
            resp.sendRedirect("/error");
            return;
        }

        Long chatId;
        try {
            chatId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            writer.println("Noto'g'ri chatId formati!");
            resp.sendRedirect("/error");
            return;
        }

        SubscriberRepo subscriberRepo = new SubscriberRepo();

        Optional<TgSubscribe> optionalSubscriber = subscriberRepo.findAll().stream()
                .filter(sub -> sub.getChat_id() != null && sub.getChat_id().equals(chatId))
                .findFirst();

        if (optionalSubscriber.isPresent()) {
            TgSubscribe subscriber = optionalSubscriber.get();
            System.out.println("subsId: " + subscriber.getChat_id());
            System.out.println("SessionID" + sessionChatId);

            if (subscriber.getChat_id().equals(Long.parseLong(id))) {
                if (Roles.ADMIN.toString().equalsIgnoreCase("Admin")) {
                    writer.println("Admin uchun alohida ish bajarilmoqda.");
                    History history = new History();
                    history.setTgSubscribe(subscriber);
                    history.setScanned_At(LocalDateTime.now());



                    HistoryRepo historyRepo = new HistoryRepo();
                    historyRepo.save(history);
                    BotService.telegramBot.execute(new SendMessage(id, String.format("Hurmatli %s, xush kelibsiz!\n🕛Kelgan vaqti: %s✅",
                            subscriber.getFirstname(),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))));

                    handleAdminActions(resp, subscriber);
                } else {
                    writer.println("Bu foydalanuvchi admin emas, lekin o'z hisobiga kirmoqda.");
                }
                return;
            }


            sendSubscriberDetails(resp, id, subscriber);
        } else {
            BotService.telegramBot.execute(new SendMessage(id, "Foydalanuvchi ma'lumotlari topilmadi."));
            resp.sendRedirect("/error");
        }
    }

    private void handleAdminActions(HttpServletResponse resp, TgSubscribe adminSubscriber) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("Adminni identifikatsiya qilish muvaffaqiyatli!");
        writer.println("Admin ismi: " + adminSubscriber.getFirstname());
        writer.println("Admin statusi: " + adminSubscriber.getStatus());
    }

    private void sendSubscriberDetails(HttpServletResponse resp, String id, TgSubscribe subscriber) throws IOException {
        String message = "Assalomu aleykum:\n" +
                "Ismi: " + subscriber.getFirstname() + "\n" +
                "Familiyasi: " + subscriber.getLastname() + "\n" +
                "Yoshi: " + subscriber.getAge() + "\n" +
                "Statusi: " + subscriber.getStatus();

        BotService.telegramBot.execute(new SendMessage(id, message));
        resp.sendRedirect("/logout");
    }
}

