package uz.pdp.gym.servlet;

import com.pengrad.telegrambot.request.SendMessage;
import jakarta.servlet.annotation.WebServlet;
import uz.pdp.gym.bot.BotService;
import uz.pdp.gym.config.Roles;
import uz.pdp.gym.config.TgSubscribe;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        TgSubscribe user = (TgSubscribe) session.getAttribute("userId");

        if (user != null && !Roles.ADMIN.equals(user.getRoles())) {
            String message = String.format("Assalomu aleykum, %s!\n\nIsmi: %s\nFamiliyasi: %s\nYoshi: %d\nStatus: %s",
                    user.getFirstname(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getAge(),
                    user.getStatus() ? "On✅" : "Off❌");
            BotService.telegramBot.execute(new SendMessage(user.getChat_id().toString(), message));

            resp.getWriter().println("Xush kelibsiz, foydalanuvchi!");
        } else {
            resp.sendRedirect("/login.jsp?error=Ruxsat yo'q");
        }
    }
}
