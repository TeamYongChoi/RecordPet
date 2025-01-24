package teamyc.recordpet.domain.healthrecord.dto;

import lombok.Builder;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;

import java.util.List;

public record MonthlyHealthRecordResponse(
        String title,
        List<HealthEvent> events) {

    @Builder
    public MonthlyHealthRecordResponse(String title, List<HealthEvent> events) {
        this.title = title;
        this.events = events;
    }

    public static MonthlyHealthRecordResponse fromEntity(HealthRecord healthRecord) {
        return MonthlyHealthRecordResponse.builder()
                .title(healthRecord.getTitle())
                .events(healthRecord.getEvents())
                .build();
    }
}
