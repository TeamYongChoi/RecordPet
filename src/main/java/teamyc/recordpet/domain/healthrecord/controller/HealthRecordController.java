package teamyc.recordpet.domain.healthrecord.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordCreateRequest;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordResponse;
import teamyc.recordpet.domain.healthrecord.dto.HealthRecordUpdateRequest;
import teamyc.recordpet.domain.healthrecord.dto.MonthlyHealthRecordResponse;
import teamyc.recordpet.domain.healthrecord.service.HealthRecordService;
import teamyc.recordpet.global.exception.CustomResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets/{petId}/health-records")
@RequiredArgsConstructor
public class HealthRecordController {

    private final HealthRecordService healthRecordService;

    @PostMapping
    public CustomResponse<Void> createHealthRecord(
            @PathVariable Long petId,
            @RequestBody HealthRecordCreateRequest request) {
        healthRecordService.save(petId, request);
        return CustomResponse.success(null);
    }

    @GetMapping("/month")
    public CustomResponse<List<MonthlyHealthRecordResponse>> findHealthRecordByMonth(
            @PathVariable Long petId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return CustomResponse.success(
                healthRecordService.findByMonthAndPetId(petId, year, month));
    }

    @GetMapping("/all")
    public CustomResponse<Page<HealthRecordResponse>> findAllHealthRecord(
            @PathVariable Long petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")[0]).descending());
        return CustomResponse.success(healthRecordService.findAllHealthRecords(petId, pageable));
    }

    @PutMapping("/{recordId}")
    public CustomResponse<Void> updateHealthRecord(
            @PathVariable Long petId,
            @PathVariable Long recordId,
            @RequestBody HealthRecordUpdateRequest request){
        healthRecordService.updateHealthRecord(petId, recordId, request);
        return CustomResponse.success(null);
    }

    @DeleteMapping("/{recordId}")
    public CustomResponse<Void> deleteHealthRecord(
            @PathVariable Long petId,
            @PathVariable Long recordId){
        healthRecordService.deleteHealthRecord(petId, recordId);
        return CustomResponse.success(null);
    }

    @DeleteMapping("/{recordId}/events/{eventId}")
    public CustomResponse<Void> deleteHealthEvent(
            @PathVariable Long petId,
            @PathVariable Long recordId,
            @PathVariable Long eventId){
        healthRecordService.deleteHealthEvent(petId, recordId, eventId);
        return CustomResponse.success(null);
    }
}
