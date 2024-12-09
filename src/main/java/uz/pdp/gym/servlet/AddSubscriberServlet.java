package uz.pdp.gym.servlet;

import uz.pdp.gym.config.Subscriber;
import uz.pdp.gym.config.TrainingTime;
import uz.pdp.gym.repo.SubscriberRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDateTime;

@MultipartConfig
@WebServlet("/add/subscriber")
public class AddSubscriberServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Formdan ma'lumotlarni olish
        String firstname = req.getParameter("firstname");
        String lastname = req.getParameter("lastname");
        String age = req.getParameter("age");
        String phone = req.getParameter("phone");
        String statusParam = req.getParameter("status");
        String trainingTimeParam = req.getParameter("trainingTime");

        // Faylni byte[] sifatida olish
        Part photoPart = req.getPart("photo");
        byte[] photoBytes = photoPart.getInputStream().readAllBytes();

        // Statusni qayta ishlash (true yoki false)
        boolean status = Boolean.parseBoolean(statusParam);

        // TrainingTime yaratish
        TrainingTime trainingTime = new TrainingTime(
                "kunlik".equals(trainingTimeParam) ? "kunlik" : null,
                "oylik".equals(trainingTimeParam) ? "oylik" : null,
                "yillik".equals(trainingTimeParam) ? "yillik" : null
        );

        // Abonementning tugash vaqtini hisoblash
        LocalDateTime subscriptionEnd = null;
        if ("oylik".equals(trainingTimeParam)) {
            subscriptionEnd = LocalDateTime.now().plusMonths(1);
        } else if ("yillik".equals(trainingTimeParam)) {
            subscriptionEnd = LocalDateTime.now().plusYears(1);
        } else if ("kunlik".equals(trainingTimeParam)) {
            subscriptionEnd = LocalDateTime.now().plusDays(1);
        }

        // Subscriber obyektini yaratish
        Subscriber subscriber = new Subscriber();
        subscriber.setFirstname(firstname);
        subscriber.setLastname(lastname);
        subscriber.setAge(Integer.parseInt(age));
        subscriber.setPhone(phone);
        subscriber.setStatus(status);
        subscriber.setRole("User");
        subscriber.setPhoto(photoBytes);
        subscriber.setTrainingTime(trainingTime);
        subscriber.setCreatedAt(LocalDateTime.now());
        subscriber.setSubscriptionEnd(subscriptionEnd);


        // Statusni yangilash
        subscriber.updateStatus();

        // Ma'lumotni bazaga saqlash
        SubscriberRepo subscriberRepo = new SubscriberRepo();
        subscriberRepo.save(subscriber);

        // Muvaffaqiyatli saqlangandan so'ng foydalanuvchini boshqa sahifaga yo'naltirish
        resp.sendRedirect("/add.jsp");
    }
}
