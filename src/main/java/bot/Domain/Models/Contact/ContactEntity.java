package bot.Domain.Models.Contact;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
public class ContactEntity {

  @Id
  private int id;
  private String Tg_Nick;
  private String phone;


}
