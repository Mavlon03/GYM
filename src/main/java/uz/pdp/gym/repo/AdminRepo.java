package uz.pdp.gym.repo;

import uz.pdp.gym.bot.DB;
import uz.pdp.gym.config.Admin;
import uz.pdp.gym.abs.BaseRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class AdminRepo extends BaseRepo<Admin> {
    public AdminRepo() {
        super(Admin.class);
    }

    public static boolean isAdminValid(String name, String password) throws SQLException {
        String query = "select count(*) from admin where name = ? and password = ?";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, password);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    public  Admin getAdminByUsernameAndPassword(String username, String password) {
        List<Admin> admins = findAll();
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
