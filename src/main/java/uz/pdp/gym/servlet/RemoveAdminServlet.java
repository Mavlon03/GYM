package uz.pdp.gym.servlet;

import jakarta.persistence.EntityManager;
import uz.pdp.gym.config.Admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static uz.pdp.gym.MyListener.EMF;

@WebServlet("/remove/admin")
public class RemoveAdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (
                EntityManager entityManager = EMF.createEntityManager();
        ) {
            String id = req.getParameter("id");
            int adminId = Integer.parseInt(id);
            entityManager.getTransaction().begin();
            Admin admin = entityManager.find(Admin.class, adminId);
            entityManager.remove(admin);
            entityManager.getTransaction().commit();
            resp.sendRedirect("/addAdmin.jsp");
        }
    }
}
