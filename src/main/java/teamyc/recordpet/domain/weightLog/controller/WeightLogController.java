package teamyc.recordpet.domain.weightLog.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import teamyc.recordpet.domain.weightLog.dto.WeightLogRegisterRequest;
import teamyc.recordpet.domain.weightLog.dto.WeightLogResponse;
import teamyc.recordpet.domain.weightLog.service.WeightLogService;
import teamyc.recordpet.global.exception.CustomResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weight")
@RequiredArgsConstructor
public class WeightLogController {

    private final WeightLogService weightLogService;

    @GetMapping
    public CustomResponse<List<WeightLogResponse>> findWeightLogByPetId(
            @RequestParam Long petId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        List<WeightLogResponse> res = weightLogService.WeightLogsFindByDateRange(petId, startDate, endDate);
        return CustomResponse.success(res);
    }

    @PostMapping
    public CustomResponse<Long> registerWeightLog(@Valid @RequestBody WeightLogRegisterRequest request){
        Long res = weightLogService.registerWeightLog(request);
        return CustomResponse.success(res);
    }
}
