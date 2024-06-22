package bot.Domain.Repositories;

import bot.Domain.Models.Appoiment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByCustomerId(int customerId);

}

