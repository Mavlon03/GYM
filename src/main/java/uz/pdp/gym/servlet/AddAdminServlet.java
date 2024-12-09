package uz.pdp.gym.servlet;

import uz.pdp.gym.config.Admin;
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
        // Formdan ma'lumotlarni olish
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String password = req.getParameter("password");

        // Admin obyektini yaratish
        Admin admin = new Admin();
        admin.setFirstname(firstname);
        admin.setLastname(lastname);
        admin.setPassword(password);
        admin.setRoles("Admin");

        // Ma'lumotni bazaga saqlash
        AdminRepo adminRepo = new AdminRepo();
        adminRepo.save(admin);

        // Muvaffaqiyatli saqlangandan so'ng foydalanuvchini boshqa sahifaga yo'naltirish
        resp.sendRedirect("/adminList.jsp"); // Adminlar ro'yxati sahifasiga yo'naltirish
    }
}
