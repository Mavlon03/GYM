package uz.pdp.gym.servlet;

import uz.pdp.gym.config.Roles;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.repo.SubscriberRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String phone = req.getParameter("phone");

        TgSubscribe tgSubscribe = SubscriberRepo.getSubscriberByPhone(phone, firstname);
        System.out.println("tgSubscribe = " + tgSubscribe);

        if (tgSubscribe != null) {
            HttpSession session = req.getSession();
            session.setAttribute("userId", tgSubscribe);

            TgSubscribe userID = (TgSubscribe) session.getAttribute("userId");
            System.out.println("userID = " + userID);
            System.out.println("USER Roles: " + userID.getRoles());
            resp.sendRedirect("/scan/qrcode");

        } else {
            resp.sendRedirect("/login.jsp");
        }
    }


}

