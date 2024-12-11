package uz.pdp.gym.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.bot.DB;
import uz.pdp.gym.config.TgSubscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static uz.pdp.gym.MyListener.EMF;

public class SubscriberRepo extends BaseRepo<TgSubscribe> {


    public SubscriberRepo() {
        super(TgSubscribe.class);
    }



    public static List<TgSubscribe> getSubscriberList(int page1, String search) {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            page1--;
            Query query = entityManager.createQuery(
                            "select s from TgSubscribe s where lower(s.firstname) LIKE lower(concat('%', :search, '%'))",
                            TgSubscribe.class)
                    .setParameter("search", search)
                    .setFirstResult(page1 * 5)
                    .setMaxResults(5);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static Long count(String search) {

        try (
                EntityManager entityManager = EMF.createEntityManager()
        ) {
            Query query = entityManager.createNativeQuery("select count(*) from TgSubscribe where firstname ilike '%' || :search || '%'", Long.class)
                    .setParameter("search", search);
            return (Long) query.getSingleResult();
        }
    }

    public static void updateExpiredSubscribersStatus() {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            List<TgSubscribe> tgSubscribes = entityManager.createQuery("select s from TgSubscribe s", TgSubscribe.class)
                    .getResultList();

            for (TgSubscribe tgSubscribe : tgSubscribes) {
                if (tgSubscribe.getSubscriptionEnd() != null && tgSubscribe.getSubscriptionEnd().isBefore(LocalDateTime.now())) {
                    tgSubscribe.setStatus(false);
                    entityManager.merge(tgSubscribe);
                }
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }
    }


    public static TgSubscribe getSubscriberByPhone(String phone) {
        System.out.println(phone);
        String query = "select * from TgSubscribe where phone = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phone);

            ResultSet resultSet = statement.executeQuery();

            // Agar subscriber topilsa
            if (resultSet.next()) {
                TgSubscribe tgSubscribe = new TgSubscribe();
                tgSubscribe.setPhone(resultSet.getString("phone"));

                return tgSubscribe;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Agar telefon raqami bo'yicha subscriber topilmasa, null qaytariladi
        return null;
    }

    public TgSubscribe findByChatId(Long chatId) {
        String query = "select * from TgSubscribe where chat_id = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, chatId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TgSubscribe tgSubscribe = new TgSubscribe();
                tgSubscribe.setChat_id(resultSet.getLong("chat_id"));

                return tgSubscribe;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

