package bot.Domain.Models.Master;

import bot.Domain.Models.Service.ServiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "masters")
public class MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false, length = 32, updatable = false, unique = true)
    private int id;
    @Column(name = "Name", nullable = false, length = 32)
    private String Name;

    @ManyToMany
    @JoinTable(
            name = "masters_services",
            joinColumns = @JoinColumn(name = "master_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<ServiceEntity> services;
}


