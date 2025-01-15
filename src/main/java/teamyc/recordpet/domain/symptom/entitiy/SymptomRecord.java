package teamyc.recordpet.domain.symptom.entitiy;

import jakarta.persistence.*;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.time.LocalDateTime;

@Entity
public class SymptomRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private LocalDateTime recordedAt;
    private String description;
}
