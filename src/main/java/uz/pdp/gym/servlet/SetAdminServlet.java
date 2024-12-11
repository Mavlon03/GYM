package uz.pdp.gym.servlet;

import uz.pdp.gym.config.Roles;
import uz.pdp.gym.repo.SubscriberRepo;
import uz.pdp.gym.config.TgSubscribe;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/set/admin")
public class SetAdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            SubscriberRepo subscriberRepo = new SubscriberRepo();
            TgSubscribe subscriber = subscriberRepo.findById(id); // ID orqali subscriber topiladi

            if (subscriber != null) {
                subscriber.setRoles(Roles.ADMIN); // Rolni ADMIN qilib o‘zgartirish
                subscriberRepo.update(subscriber); // Yangilash
                resp.sendRedirect("/addAdmin.jsp"); // Yangilangan ma‘lumotlarni qaytadan yuklash
            } else {
                resp.getWriter().println("Subscriber not found!");
            }
        } else {
            resp.getWriter().println("Invalid request!");
        }
    }
}
