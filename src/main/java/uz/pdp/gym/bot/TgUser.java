package uz.pdp.gym.bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TgUser {
    private Long chatId;
    private String phone;
    private TgState tgState = TgState.START;

    public TgUser(Long chatId, String phone) {
        this.chatId = chatId;
        this.phone = phone;
    }
}
