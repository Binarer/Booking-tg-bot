package bot.Domain.Models.Service;


import bot.Domain.Models.Master.MasterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, length = 32, updatable = false, unique = true)
    private int Id;

    @Column(name = "name", nullable = false)
    private String Name;

    @Column(name = "price", nullable = false)
    private float Price;

    @Column(name = "duration", nullable = false)
    private int Duration;

    @ManyToMany(mappedBy = "services")
    private List<MasterEntity> masters;
}


