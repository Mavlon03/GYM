package uz.pdp.gym.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.config.Subscriber;

import java.time.LocalDateTime;
import java.util.List;

import static uz.pdp.gym.MyListener.EMF;

public class SubscriberRepo extends BaseRepo<Subscriber> {


    public SubscriberRepo() {
        super(Subscriber.class);
    }



    public static List<Subscriber> getSubscriberList(int page1, String search) {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            page1--;
            Query query = entityManager.createQuery(
                            "select s from Subscriber s WHERE LOWER(s.firstname) LIKE LOWER(CONCAT('%', :search, '%'))",
                            Subscriber.class)
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
            Query query = entityManager.createNativeQuery("select count(*) from subscriber where firstname ilike '%' || :search || '%'", Long.class)
                    .setParameter("search", search);
            return (Long) query.getSingleResult();
        }
    }

    public static void updateExpiredSubscribersStatus() {
        EntityManager entityManager = EMF.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            List<Subscriber> subscribers = entityManager.createQuery("select s from Subscriber s", Subscriber.class)
                    .getResultList();

            for (Subscriber subscriber : subscribers) {
                if (subscriber.getSubscriptionEnd() != null && subscriber.getSubscriptionEnd().isBefore(LocalDateTime.now())) {
                    subscriber.setStatus(false);
                    entityManager.merge(subscriber);
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
    List<Subscriber> subscribers = findAll();
    public Subscriber findById(Integer subscriberId) {
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getId().equals(subscriberId)) {
                return subscriber;
            }
        }
        return null;
    }

    public Subscriber findByPhone(String phone) {
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getPhone().equals(phone)) {
                return subscriber;
            }
        }
        return null;
    }
}
