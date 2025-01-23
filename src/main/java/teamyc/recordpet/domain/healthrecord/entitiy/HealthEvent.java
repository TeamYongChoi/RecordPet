package teamyc.recordpet.domain.healthrecord.entitiy;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class HealthEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime occurrenceTime;

    @Lob
    private String content;

    @Builder
    public HealthEvent(LocalDateTime occurrenceTime, String content){
        this.occurrenceTime = occurrenceTime;
        this.content = content;
    }
}
