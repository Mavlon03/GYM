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



}
