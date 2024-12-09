package uz.pdp.gym.repo;

import uz.pdp.gym.config.Admin;
import uz.pdp.gym.abs.BaseRepo;

import java.util.List;

public class AdminRepo extends BaseRepo<Admin> {
    public AdminRepo() {
        super(Admin.class);
    }

    List<Admin> admins = findAll();
    public Admin findByUsernameAndPassword(String username, String password) {
        for (Admin admin : admins) {
            System.out.println(admin.getFirstname());
            System.out.println(admin);
            if (admin.getFirstname().equalsIgnoreCase(username)&&admin.getPassword().equals(password)){
            return admin;
            }
        }
        return null;
    }
}
