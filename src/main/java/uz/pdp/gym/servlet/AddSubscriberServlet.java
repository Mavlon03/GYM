package uz.pdp.gym.servlet;

import uz.pdp.gym.config.Roles;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.config.TrainingTime;
import uz.pdp.gym.repo.SubscriberRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@MultipartConfig
@WebServlet("/add/tgSubscribe")
public class AddSubscriberServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String age = req.getParameter("age");
        String phone = req.getParameter("phone");
        String statusParam = req.getParameter("status");
        String trainingTimeParam = req.getParameter("trainingTime");
        Part photoPart = req.getPart("photo");
        byte[] photoBytes = photoPart.getInputStream().readAllBytes();

        boolean status = Boolean.parseBoolean(statusParam);
        TrainingTime trainingTime = new TrainingTime(
                "kunlik".equals(trainingTimeParam) ? "kunlik" : null,
                "oylik".equals(trainingTimeParam) ? "oylik" : null,
                "yillik".equals(trainingTimeParam) ? "yillik" : null
        );

        LocalDateTime subscriptionEnd = null;
        if ("oylik".equals(trainingTimeParam)) {
            subscriptionEnd = LocalDateTime.now().plusMonths(1);
        } else if ("yillik".equals(trainingTimeParam)) {
            subscriptionEnd = LocalDateTime.now().plusYears(1);
        } else if ("kunlik".equals(trainingTimeParam)) {
            subscriptionEnd = LocalDateTime.now().plusDays(1);
        }

        TgSubscribe tgSubscribe = new TgSubscribe();
        tgSubscribe.setFirstname(firstname);
        tgSubscribe.setLastname(lastname);
        tgSubscribe.setAge(Integer.parseInt(age));
        tgSubscribe.setPhone(phone);
        tgSubscribe.setStatus(status);
        tgSubscribe.setRoles(Roles.USER);
        tgSubscribe.setPhoto(photoBytes);
        tgSubscribe.setTrainingTime(trainingTime);
        tgSubscribe.setCreatedAt(LocalDateTime.now());
        tgSubscribe.setSubscriptionEnd(subscriptionEnd);

        tgSubscribe.updateStatus();

        SubscriberRepo subscriberRepo = new SubscriberRepo();
        subscriberRepo.save(tgSubscribe);


        resp.sendRedirect("/add.jsp");
    }
}
