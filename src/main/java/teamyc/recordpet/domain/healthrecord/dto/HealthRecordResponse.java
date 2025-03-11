package teamyc.recordpet.domain.healthrecord.dto;

import lombok.Builder;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

public record HealthRecordResponse(
        String title,
        HealthEvent firstEvent) {

    @Builder
    public HealthRecordResponse(String title, HealthEvent firstEvent) {
        this.title = title;
        this.firstEvent = firstEvent;
    }

    public static HealthRecordResponse fromEntity(HealthRecord healthRecord) {
        return HealthRecordResponse.builder()
                .title(healthRecord.getTitle())
                .firstEvent(healthRecord.getEvents().isEmpty() ? null : healthRecord.getEvents().get(0))
                .build();
    }
}
