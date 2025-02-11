package teamyc.recordpet.domain.healthrecord.dto;

import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

import java.time.LocalDateTime;

public record HealthEventUpdateRequest(
        Long id,
        LocalDateTime occurrenceTime,
        String content) {

    public HealthEvent toEntity(HealthRecord healthRecord) {
        return HealthEvent.builder()
                .occurrenceTime(this.occurrenceTime)
                .content(this.content)
                .healthRecord(healthRecord)
                .build();
    }
}
