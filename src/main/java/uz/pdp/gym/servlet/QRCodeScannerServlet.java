package uz.pdp.gym.servlet;

import uz.pdp.gym.bot.BotService;
import uz.pdp.gym.config.Admin;
import uz.pdp.gym.config.History;
import uz.pdp.gym.config.TgSubscribe;
import uz.pdp.gym.repo.HistoryRepo;
import uz.pdp.gym.repo.SubscriberRepo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
//@WebServlet("scan/qrcode")
public class QRCodeScannerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String chatIdStr = req.getParameter("chatId");
        if (chatIdStr == null || chatIdStr.isEmpty()) {
            resp.getWriter().write("Invalid QR code data!");
            return;
        }
        Long chatId = Long.parseLong(chatIdStr);

        SubscriberRepo tgSubscribeRepo = new SubscriberRepo();
        TgSubscribe user = tgSubscribeRepo.findByChatId(chatId);
        System.out.println(user.getFirstname());
        System.out.println(user.getChat_id());
        if (user == null) {
            resp.getWriter().write("User not found!");
            return;
        }

        HttpSession session = req.getSession();
        Admin admin = (Admin) session.getAttribute("role");

        if (admin == null) {
            resp.getWriter().write("You are not authorized to scan QR codes!");
            return;
        }

        History history = new History();
        history.setTgSubscribe(user);
        history.setAdmin(admin);
        history.setScanned_At(LocalDateTime.now());

        HistoryRepo historyRepo = new HistoryRepo();
        historyRepo.save(history);

        BotService.sendQRCodeScannedNotification(user, admin);

        resp.getWriter().write("QR code scanned successfully!");
    }
}

