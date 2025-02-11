package teamyc.recordpet.domain.healthrecord.entitiy;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "health_record")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private String title;

    @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<HealthEvent> events = new ArrayList<>();

    public HealthRecord(Pet pet, String title, List<HealthEvent> events) {
        this.pet = pet;
        this.title = title;
        this.events = events;
    }

    public void addEvent(HealthEvent event) {
        events.add(event);
        event.setHealthRecord(this);  // 양방향 관계 설정
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    public void updateEvents(List<HealthEvent> updatedEvents) {
        this.events.clear();
        this.events.addAll(updatedEvents);
    }

    public void removeEvent(HealthEvent event) {
        this.events.remove(event);
    }
}
