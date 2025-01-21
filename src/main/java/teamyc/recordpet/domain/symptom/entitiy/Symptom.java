package teamyc.recordpet.domain.symptom.entitiy;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Symptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String description;

    @Builder
    public Symptom(LocalDateTime createdAt, LocalDateTime updatedAt, String description) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
    }
}
