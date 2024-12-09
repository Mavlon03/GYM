package uz.pdp.gym.servlet;

import jakarta.persistence.EntityManager;
import uz.pdp.gym.config.Subscriber;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static uz.pdp.gym.MyListener.EMF;

@WebServlet("/remove")
public class RemoveSubscriberServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try (
                EntityManager entityManager = EMF.createEntityManager();
        ) {
            String id = req.getParameter("id");
            int subscriberId = Integer.parseInt(id);
            entityManager.getTransaction().begin();
            Subscriber subscriber = entityManager.find(Subscriber.class, subscriberId);
            entityManager.remove(subscriber);
            entityManager.getTransaction().commit();
            resp.sendRedirect("/add.jsp");
        }
    }
}
