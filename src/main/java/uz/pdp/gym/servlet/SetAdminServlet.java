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
        String chat_id = req.getParameter("id");

        if (chat_id != null) {
            int id = Integer.parseInt(chat_id);
            SubscriberRepo subscriberRepo = new SubscriberRepo();
            TgSubscribe subscriber = subscriberRepo.findById(id);

            if (subscriber != null) {
                if (Roles.ADMIN.equals(subscriber.getRoles())) {
                    subscriber.setRoles(Roles.USER);
                } else {
                    subscriber.setRoles(Roles.ADMIN);
                }
                subscriberRepo.update(subscriber);
                resp.sendRedirect("/addAdmin.jsp");
            } else {
                resp.getWriter().println("Subscriber not found!");
            }
        } else {
            resp.getWriter().println("Invalid request!");
        }
    }
}
