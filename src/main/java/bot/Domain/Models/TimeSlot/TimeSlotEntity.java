package bot.Domain.Models.TimeSlot;

import bot.Domain.Models.Master.MasterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Slots")
public class TimeSlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, length = 32, updatable = false, unique = true)
    private int id;

    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private MasterEntity master;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "booked", nullable = false)
    private boolean booked;
}
