package uz.pdp.gym.servlet;

import com.pengrad.telegrambot.request.SendMessage;
import jakarta.servlet.annotation.WebServlet;
import uz.pdp.gym.bot.BotService;
import uz.pdp.gym.config.History;
import uz.pdp.gym.config.Roles;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.repo.HistoryRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        TgSubscribe admin = (TgSubscribe) session.getAttribute("userId");

        if (admin != null && Roles.ADMIN.equals(admin.getRoles())) {
            History history = new History();
            history.setTgSubscribe(admin);
            history.setScanned_At(LocalDateTime.now());
            new HistoryRepo().save(history);

            String message = String.format("Hurmatli %s, tizimga kirdingiz!\n🕛 Kelgan vaqti: %s ✅",
                    admin.getFirstname(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            BotService.telegramBot.execute(new SendMessage(admin.getChat_id().toString(), message));

            resp.getWriter().println("Xush kelibsiz, admin!");
        } else {
            resp.sendRedirect("/login.jsp?error=Ruxsat yo'q");
        }
    }
}
