package bot.Domain.Models.Client;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity(name = "Customer")
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, length = 16, updatable = false,unique = true)
    private long Id;

    @Column(nullable = false)
    private String Name;

    @Column(name = "tg", nullable = false)
    private String telegram;
}
