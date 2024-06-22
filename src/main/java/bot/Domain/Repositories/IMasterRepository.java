package bot.Domain.Repositories;

import bot.Domain.Models.Master.MasterEntity;
import bot.Domain.Models.Service.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMasterRepository extends JpaRepository<MasterEntity, Integer> {
    List<MasterEntity> findByServices(ServiceEntity service);

    @Query("SELECT m FROM MasterEntity m JOIN m.services as s WHERE s.Id = :serviceId")
    List<MasterEntity> findByServicesId(@Param("serviceId") int serviceId);
}
