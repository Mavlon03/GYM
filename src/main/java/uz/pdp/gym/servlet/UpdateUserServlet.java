package uz.pdp.gym.servlet;

import jakarta.persistence.EntityManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static uz.pdp.gym.MyListener.EMF;

@WebServlet("/update/")
public class UpdateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(
                EntityManager entityManager = EMF.createEntityManager()
        ) {
            int id = Integer.parseInt(req.getParameter("id"));
            String name = req.getParameter("name");
            String countryId = req.getParameter("countryId");
//            entityManager.getTransaction().begin();
//            Region region = entityManager.find(Region.class, id);
//            region.setName(name);
//            region.setCountry(country);
            entityManager.getTransaction().commit();
            resp.sendRedirect("/add.jsp");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    }
