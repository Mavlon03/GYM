package uz.pdp.gym.config;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.gym.abs.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin extends BaseEntity {
    private String firstname;
    private String lastname;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles roles ;

}
