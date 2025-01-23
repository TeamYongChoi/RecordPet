package teamyc.recordpet.domain.healthrecord.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordCreateRequest;
import teamyc.recordpet.domain.healthrecord.service.HealthRecordService;
import teamyc.recordpet.global.exception.CustomResponse;

@RestController
@RequestMapping("/api/v1/pets/{petId}/health-records")
@RequiredArgsConstructor
public class HealthRecordController {

    private final HealthRecordService symptomService;

    @PostMapping
    public CustomResponse<Void> createHealthRecord(
            @PathVariable Long petId,
            @RequestBody HealthRecordCreateRequest request) {
        symptomService.save(petId, request);
        return CustomResponse.success(null);
    }
}
