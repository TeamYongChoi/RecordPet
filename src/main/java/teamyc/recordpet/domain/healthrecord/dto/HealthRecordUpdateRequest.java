package teamyc.recordpet.domain.healthrecord.dto;

import java.util.ArrayList;
import java.util.List;

public record HealthRecordUpdateRequest(
        String title,
        List<HealthEventUpdateRequest> events) {
    public HealthRecordUpdateRequest {
        if (events == null) {
            events = new ArrayList<>();
        }
    }
}

