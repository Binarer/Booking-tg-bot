package bot.Domain.Repositories;

import bot.Domain.Models.Client.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {
    Customer findCustomerByTelegram(String telegram);
}
