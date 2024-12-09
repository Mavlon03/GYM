package uz.pdp.gym.config;

import jakarta.persistence.*;
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
    private Boolean status;

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
