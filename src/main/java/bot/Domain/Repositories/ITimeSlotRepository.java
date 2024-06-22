package bot.Domain.Repositories;

import bot.Domain.Models.Master.MasterEntity;
import bot.Domain.Models.TimeSlot.TimeSlotEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITimeSlotRepository extends CrudRepository<TimeSlotEntity, Integer> {
    List<TimeSlotEntity> findByMasterAndDateAndBooked(MasterEntity master, LocalDate date, boolean booked);

    TimeSlotEntity findByMasterAndStartTimeAndDate(MasterEntity master, LocalTime time, LocalDate date);
}
