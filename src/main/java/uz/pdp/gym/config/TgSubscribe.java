package uz.pdp.gym.config;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uz.pdp.gym.abs.BaseEntity;
import uz.pdp.gym.bot.DB;
import uz.pdp.gym.bot.TgState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TgSubscribe")
@Entity
public class TgSubscribe extends BaseEntity {
    private String firstname;
    private String lastname;
    private Integer age;
    private String phone;
    private Boolean status;
    @Lob // QR code tasviri ko'p joy egallashi mumkin, shuning uchun @Lob ishlatiladi.
    private byte[] qr_code;
    private Long chat_id;

    @Enumerated(EnumType.STRING)
    private Roles roles ;

    @Lob
    private byte[] photo;

    @Embedded
    private TrainingTime trainingTime;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "subscription_end")
    private LocalDateTime subscriptionEnd;

    @Enumerated(EnumType.STRING)
    private TgState tgState = TgState.START;


    public void updateStatus() {
        if (this.subscriptionEnd != null) {
            System.out.println("Subscription End: " + this.subscriptionEnd);
            System.out.println("Current Time: " + LocalDateTime.now());
            if (this.subscriptionEnd.isBefore(LocalDateTime.now())) {
                this.status = false;
                System.out.println("Status updated to false.");
            }
        }
    }

}
