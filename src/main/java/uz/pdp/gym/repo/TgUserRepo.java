package uz.pdp.gym.repo;

import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.bot.TgUser;
import uz.pdp.gym.config.Subscriber;

public class TgUserRepo extends BaseRepo<TgUser> {
    public TgUserRepo() {
        super(TgUser.class);
    }

}
