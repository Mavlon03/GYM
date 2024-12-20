package uz.pdp.gym.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.bot.DB;
import uz.pdp.gym.bot.TgState;
import uz.pdp.gym.config.Roles;
import uz.pdp.gym.config.TgSubscribe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static uz.pdp.gym.MyListener.EMF;

public class SubscriberRepo extends BaseRepo<TgSubscribe> {


    public SubscriberRepo() {
        super(TgSubscribe.class);
    }

    public static TgSubscribe getSubscriberById(Long chatId) {
        String query = "select * from TgSubscribe where chat_id = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, chatId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TgSubscribe subscriber = new TgSubscribe();
                subscriber.setChat_id(resultSet.getLong("chat_id"));
                subscriber.setFirstname(resultSet.getString("firstname"));
                subscriber.setLastname(resultSet.getString("lastname"));
                return subscriber;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TgSubscribe findById(int id) {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            return entityManager.find(TgSubscribe.class, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(TgSubscribe subscriber) {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.merge(subscriber);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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


    public static TgSubscribe getSubscriberByPhone(String phone, String firstname) {
        String query = "SELECT * FROM TgSubscribe WHERE phone = ? AND firstname = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, phone);
            statement.setString(2, firstname);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                TgSubscribe tgSubscribe = new TgSubscribe();
                tgSubscribe.setPhone(resultSet.getString("phone"));
                tgSubscribe.setFirstname(resultSet.getString("firstname"));
                tgSubscribe.setAge(resultSet.getInt("age"));
                tgSubscribe.setChat_id(resultSet.getLong("chat_id"));
                tgSubscribe.setStatus(resultSet.getBoolean("status"));
                tgSubscribe.setId(resultSet.getInt("id"));
                tgSubscribe.setRoles(Roles.valueOf(resultSet.getString("roles")));
                tgSubscribe.setLastname(resultSet.getString("lastname"));

                return tgSubscribe;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Object[]> dailySubscribers() {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            return entityManager.createNativeQuery("""
                    select
           to_char(s.createdat, 'YYYY-MM-DD' ) as date,
           count(*) as visitor_count from tgsubscribe as s
       group by
           to_char(s.createdat, 'YYYY-MM-DD')
       order by date;
        """).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving daily subscribers", e);
        }
    }

}

