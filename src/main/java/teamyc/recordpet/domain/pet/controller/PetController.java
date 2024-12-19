package teamyc.recordpet.domain.pet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamyc.recordpet.domain.pet.dto.PetRegisterRequest;
import teamyc.recordpet.domain.pet.dto.PetResponse;
import teamyc.recordpet.domain.pet.dto.PetUpdateRequest;
import teamyc.recordpet.domain.pet.dto.PetUpdateResponse;
import teamyc.recordpet.domain.pet.service.PetService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponse> addPet(@RequestBody PetRegisterRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(petService.PetProfileSave(req));
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> findAllPets() {
        return ResponseEntity
                .ok()
                .body(petService.PetProfileFindAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetResponse> findPetById(@PathVariable Long id) {
        PetResponse res = petService.PetProfileFindById(id);

        return ResponseEntity.ok()
                .body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetUpdateResponse> updatePet(@PathVariable Long id, @RequestBody PetUpdateRequest req) {
        PetUpdateResponse res = petService.PetProfileUpdate(id, req);

        return ResponseEntity.ok()
                .body(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.PetProfileDelete(id);

        return ResponseEntity.ok()
                .build();
    }
}
