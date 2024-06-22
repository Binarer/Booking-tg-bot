package bot.Domain.Repositories;

import bot.Domain.Models.Service.ServiceEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IServicesRepository extends CrudRepository<ServiceEntity, Integer> {
    Optional<ServiceEntity> findFirstByOrderByIdAsc();
}
