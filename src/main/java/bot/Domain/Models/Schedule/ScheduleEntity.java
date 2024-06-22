package bot.Domain.Models.Schedule;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Schedules")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id", nullable = false, length = 32, updatable = false, unique = true)
    private int Id;

    @Column(name = "Master_id", nullable = false, length = 32, updatable = false)
    private int Master_id;

    @Column(name = "timeSlots", nullable = false, length = 32, updatable = false)
    private int timeSlots;

}
