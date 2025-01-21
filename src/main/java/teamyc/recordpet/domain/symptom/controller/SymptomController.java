package teamyc.recordpet.domain.symptom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamyc.recordpet.domain.symptom.dto.SymptomCreateRequest;
import teamyc.recordpet.domain.symptom.service.SymptomService;
import teamyc.recordpet.global.exception.CustomResponse;

@RestController
@RequestMapping("/api/v1/pets/{petId}/symptoms")
@RequiredArgsConstructor
public class SymptomController {

    private final SymptomService symptomService;

    @PostMapping
    public CustomResponse<Void> createSymptom(
            @PathVariable Long petId,
            @RequestBody SymptomCreateRequest request) {
        symptomService.save(petId, request);
        return CustomResponse.success(null);
    }
}
