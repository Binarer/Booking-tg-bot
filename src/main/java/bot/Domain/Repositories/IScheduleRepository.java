package bot.Domain.Repositories;

import bot.Domain.Models.Schedule.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IScheduleRepository extends CrudRepository<ScheduleEntity, Integer> {
}
