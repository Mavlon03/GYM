package uz.pdp.gym.servlet;

import com.pengrad.telegrambot.request.SendMessage;
import uz.pdp.gym.bot.BotService;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.repo.SubscriberRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@WebServlet("/scan/qrcode")
public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("chatId");
        HttpSession session = req.getSession();
        Object role = session.getAttribute("role");
        SubscriberRepo subscriberRepo = new SubscriberRepo();

        if (role == null) {
            Optional<TgSubscribe> optionalSubscriber = subscriberRepo.findAll().stream()
                    .filter(sub -> sub.getChat_id() != null && sub.getChat_id().equals(Long.parseLong(id)))
                    .findFirst();

            if (optionalSubscriber.isPresent()) {
                TgSubscribe subscriber = optionalSubscriber.get();

                String message = "Assalomu aleykum:\n" +
                        "Ismi: " + subscriber.getFirstname() + "\n" +
                        "Familiyasi: " + subscriber.getLastname() + "\n" +
                        "Yoshi: " + subscriber.getAge() + "\n" +
                        "Statusi: " + (subscriber.getStatus() ? "Aktiv" : "Nofaol");

                SendMessage sendMessage = new SendMessage(id, message);
                BotService.telegramBot.execute(sendMessage);

                resp.sendRedirect("/logout");
            } else {
                SendMessage sendMessage = new SendMessage(id, "Foydalanuvchi ma'lumotlari topilmadi.");
                BotService.telegramBot.execute(sendMessage);
                resp.sendRedirect("/error");
            }
        } else {
            resp.getWriter().println("Bu admin foydalanuvchi.");
        }
    }
}
