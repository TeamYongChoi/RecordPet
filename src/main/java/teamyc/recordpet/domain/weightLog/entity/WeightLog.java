package teamyc.recordpet.domain.weightLog.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class WeightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private Double weight;

    private LocalDate date;

    @Builder
    WeightLog(Pet pet, Double weight, LocalDate  date){
        this.pet = pet;
        this.weight = weight;
        this.date = date != null ? date : LocalDate.now();
    }

}
