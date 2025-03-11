package teamyc.recordpet.domain.healthrecord.dto;

import lombok.Builder;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

import java.util.List;

public record DailyHealthRecordResponse(
        String title,
        List<HealthEvent> events) {

    @Builder
    public DailyHealthRecordResponse(String title, List<HealthEvent> events) {
        this.title = title;
        this.events = events;
    }

    public static DailyHealthRecordResponse fromEntity(HealthRecord healthRecord) {
        return DailyHealthRecordResponse.builder()
                .title(healthRecord.getTitle())
                .events(healthRecord.getEvents())
                .build();
    }
}
