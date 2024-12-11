package uz.pdp.gym.repo;

import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.bot.DB;
import uz.pdp.gym.config.History;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoryRepo extends BaseRepo<History> {

    public HistoryRepo() {
        super(History.class);
    }

    public static void addScanHistory(int subscriberId, int adminId) throws SQLException {
        String query = "inser into history (subscriber_id, admin_id) values (?, ?)";
        try (Connection connection = DB.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, subscriberId);
            statement.setInt(2, adminId);
            statement.executeUpdate();
        }
    }

}
