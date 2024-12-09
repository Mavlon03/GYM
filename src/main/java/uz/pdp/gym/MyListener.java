package uz.pdp.gym;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class MyListener implements ServletContextListener {
    public static EntityManagerFactory EMF;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        EMF = Persistence.createEntityManagerFactory("default");
    }
}
