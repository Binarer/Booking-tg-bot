package bot.Domain.Models.Appoiment;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", nullable = false, length = 32, updatable = false, unique = true)
    private int Id;

    private int customerId;

    private int masterId;

    private int slotId;

    private int serviceId;
    private Object object;

    public int getCustomerId() {
        object.
    }
}
