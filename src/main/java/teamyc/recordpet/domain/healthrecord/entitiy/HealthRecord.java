package teamyc.recordpet.domain.healthrecord.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "health_record_id")
    private List<HealthEvent> events = new ArrayList<>();

    @Builder
    public HealthRecord(String title, List<HealthEvent> events) {
        this.title = title;
        this.events = events;
    }
}
