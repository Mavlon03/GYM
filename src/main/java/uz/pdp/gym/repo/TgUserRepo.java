package uz.pdp.gym.repo;

import uz.pdp.gym.abs.BaseRepo;
import uz.pdp.gym.config.TgSubscribe;

public class TgUserRepo extends BaseRepo<TgSubscribe> {
    public TgUserRepo() {
        super(TgSubscribe.class);
    }

}
