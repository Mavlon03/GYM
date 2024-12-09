package uz.pdp.gym.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.config.Subscriber;

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
                            "SELECT s FROM Subscriber s WHERE LOWER(s.firstname) LIKE LOWER(CONCAT('%', :search, '%'))",
                            Subscriber.class)
                    .setParameter("search", search)
                    .setFirstResult(page1 * 10)
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
}
