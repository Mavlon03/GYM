package uz.pdp.gym.config;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.gym.abs.BaseEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class History extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private TgSubscribe tgSubscribe;
    private LocalDateTime scanned_At;
}
