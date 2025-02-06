package teamyc.recordpet.domain.healthrecord.dto;

import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;
import teamyc.recordpet.domain.pet.entity.Pet;

import java.util.List;
import java.util.stream.Collectors;

public record HealthRecordCreateRequest(Pet pet, String title, List<HealthEventCreateRequest> events) {

    public HealthRecord toEntity(Pet pet) {
        return HealthRecord.builder()
                .pet(pet)
                .title(this.title)
                .events(this.events.stream()
                        .map(HealthEventCreateRequest::toEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
