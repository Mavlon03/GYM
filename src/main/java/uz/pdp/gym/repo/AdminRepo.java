package uz.pdp.gym.repo;

import uz.pdp.gym.config.Admin;
import uz.pdp.gym.abs.BaseRepo;

public class AdminRepo extends BaseRepo<Admin> {
    public AdminRepo() {
        super(Admin.class);
    }
}
