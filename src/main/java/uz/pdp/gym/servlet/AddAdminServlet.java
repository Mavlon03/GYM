package uz.pdp.gym.servlet;

import uz.pdp.gym.config.Admin;
import uz.pdp.gym.config.Roles;
import uz.pdp.gym.repo.AdminRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add/admin")
public class AddAdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String password = req.getParameter("password");

        Admin admin = new Admin();
        admin.setFirstname(firstname);
        admin.setLastname(lastname);
        admin.setPassword(password);
        admin.setRoles(Roles.ADMIN);

        AdminRepo adminRepo = new AdminRepo();
        adminRepo.save(admin);

        resp.sendRedirect("/addAdmin.jsp");
    }
}
