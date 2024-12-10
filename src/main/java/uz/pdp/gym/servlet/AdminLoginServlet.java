package uz.pdp.gym.servlet;

import jakarta.servlet.annotation.WebServlet;
import uz.pdp.gym.config.Admin;
import uz.pdp.gym.repo.AdminRepo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String firstname = req.getParameter("firstname");
        String password = req.getParameter("password");

        AdminRepo adminRepo = new AdminRepo();
        Admin admin = adminRepo.getAdminByUsernameAndPassword(firstname, password);

        if (admin != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("admin", admin);
            resp.sendRedirect("scanQRCode.jsp");
        } else {
            resp.setContentType("text/html");
            resp.getWriter().println("<html><body><h3>Login yoki parol noto'g'ri!</h3></body></html>");
        }
    }
}
