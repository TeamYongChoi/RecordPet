package teamyc.recordpet.domain.healthrecord.dto;

import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;

import java.time.LocalDateTime;

public record HealthEventCreateRequest (LocalDateTime occurrenceTime,
                                        String content) {
    public HealthEvent toEntity(){
        return HealthEvent.builder()
                .occurrenceTime(this.occurrenceTime)
                .content(this.content)
                .build();
    }
}
