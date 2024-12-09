package uz.pdp.gym.config;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.gym.abs.BaseEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Subscriber extends BaseEntity {
    private String firstname;
    private String lastname;
    private Integer age;
    private String phone;
    private Boolean status; // "on/off" ni boolean sifatida saqlaymiz
    private String  role;

    @Lob
    private byte[] photo;

    @Embedded
    private TrainingTime trainingTime; // kunlik, oylik, yillik

    @CreationTimestamp
    private LocalDateTime createdAt; // Foydalanuvchi ro'yxatdan o'tgan vaqt

    private LocalDateTime subscriptionEnd;

    public void updateStatus() {
        if (subscriptionEnd != null && LocalDateTime.now().isAfter(subscriptionEnd)) {
            this.status = false; // Muddat tugagan bo'lsa, "Off"
        } else {
            this.status = true; // Hozirgi vaqtga qadar abonement faol bo'lsa, "On"
        }
    }

}
