package teamyc.recordpet.domain.pet.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamyc.recordpet.domain.pet.dto.GetPetDetailProfileResponse;
import teamyc.recordpet.domain.pet.dto.GetPetProfileResponse;
import teamyc.recordpet.domain.pet.dto.PetRegisterRequest;
import teamyc.recordpet.domain.pet.dto.PetRegisterResponse;
import teamyc.recordpet.domain.pet.dto.PetUpdateRequest;
import teamyc.recordpet.domain.pet.dto.PetUpdateResponse;
import teamyc.recordpet.domain.pet.service.PetService;
import teamyc.recordpet.global.exception.CustomResponse;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping("/{userId}")
    public CustomResponse<PetRegisterResponse> addPet(
        @PathVariable Long userId,
        @RequestPart("req") PetRegisterRequest req,  // JSON 데이터
        @RequestPart("profileImage") MultipartFile profileImage) {
        return CustomResponse.created(petService.savePetProfile(userId, req, profileImage));
    }

    @GetMapping("/{userId}")
    public CustomResponse<List<GetPetProfileResponse>> findAllPets(@PathVariable Long userId) {
        return CustomResponse.success(petService.getAllPetProfile(userId));
    }

    @GetMapping("/{userId}/{petId}")
    public CustomResponse<GetPetDetailProfileResponse> findPetById(@PathVariable Long userId,
        @PathVariable Long petId) {
        GetPetDetailProfileResponse res = petService.getPetDetailProfile(userId, petId);

        return CustomResponse.success(res);
    }

    @PutMapping("/{userId}/{petId}")
    public CustomResponse<PetUpdateResponse> updatePet(@PathVariable Long userId,
        @PathVariable Long petId,
        @RequestPart("req") PetUpdateRequest req,
        @RequestPart("profileImage") MultipartFile profileImage) {
        return CustomResponse.success(
            petService.updatePetProfile(userId, petId, req, profileImage));
    }

    @DeleteMapping("/{userId}/{petId}")
    public CustomResponse<Void> deletePet(@PathVariable Long userId, @PathVariable Long petId) {
        petService.deletePetProfile(userId, petId);

        return CustomResponse.success(null);
    }
}
