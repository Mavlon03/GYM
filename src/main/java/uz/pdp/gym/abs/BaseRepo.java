package uz.pdp.gym.abs;

import jakarta.persistence.EntityManager;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import static uz.pdp.gym.MyListener.EMF;
public class BaseRepo<T> {

    private final Class<T> persistenceClass;

    public BaseRepo(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    public void save(T entity) {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  List<T> findAll() {
        try (EntityManager entityManager = EMF.createEntityManager()) {
            return entityManager.createQuery("from " + persistenceClass.getSimpleName(), persistenceClass)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
