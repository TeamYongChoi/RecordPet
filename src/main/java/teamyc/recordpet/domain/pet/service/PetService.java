package teamyc.recordpet.domain.pet.service;

import static teamyc.recordpet.global.exception.ResultCode.BAD_REQUEST;
import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_PET_PROFILE;
import static teamyc.recordpet.global.exception.ResultCode.NOT_FOUND_USER;
import static teamyc.recordpet.global.exception.ResultCode.SYSTEM_ERROR;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teamyc.recordpet.domain.pet.dto.GetPetDetailProfileResponse;
import teamyc.recordpet.domain.pet.dto.GetPetProfileResponse;
import teamyc.recordpet.domain.pet.dto.PetRegisterRequest;
import teamyc.recordpet.domain.pet.dto.PetRegisterResponse;
import teamyc.recordpet.domain.pet.dto.PetUpdateRequest;
import teamyc.recordpet.domain.pet.dto.PetUpdateResponse;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.domain.user.entity.User;
import teamyc.recordpet.domain.user.repository.UserRepository;
import teamyc.recordpet.global.exception.GlobalException;
import teamyc.recordpet.global.image.ProfileImageRepository;
import teamyc.recordpet.global.image.entity.ProfileImage;
import teamyc.recordpet.global.image.entity.Type;
import teamyc.recordpet.global.s3.S3Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class PetService {

    private final PetRepository petRepository;
    private final S3Service s3Service;
    private final ProfileImageRepository profileImageRepository;
    private final UserRepository userRepository;

    // user의 pet 전체 조회
    public List<GetPetProfileResponse> getAllPetProfile(Long userId) {
        List<Pet> petList = petRepository.findAllByUserId(userId);

        if (petList.isEmpty()) {
            throw new GlobalException(NOT_FOUND_PET_PROFILE);
        }

        return petList.stream()
            .map(GetPetProfileResponse::fromEntity)
            .collect(Collectors.toList());
    }

    // user pet 상세 조회
    public GetPetDetailProfileResponse getPetDetailProfile(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));

        if (!userId.equals(pet.getUser().getId())) {
            throw new GlobalException(BAD_REQUEST);
        }

        return GetPetDetailProfileResponse.fromEntity(pet);
    }

    //등록
    public PetRegisterResponse savePetProfile(Long userId, PetRegisterRequest req,
        MultipartFile profileImage) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_USER));

        // 사용자가 직접 프로필 업로드 하는 경우
        if (!profileImage.isEmpty()) {
            String profileImageUrl = s3Service.uploadImage(profileImage, "pet-profile-images");

            ProfileImage image = ProfileImage.builder()
                .type(Type.PET)
                .isBasic(false)
                .imageUrl(profileImageUrl)
                .build();

            profileImageRepository.save(image);
            petRepository.save(req.toEntity(user, image));

            return new PetRegisterResponse();

        }

        ProfileImage image = profileImageRepository.findBasicImage(Type.PET)
            .orElseThrow(() -> new GlobalException(SYSTEM_ERROR));

        petRepository.save(req.toEntity(user, image));

        return new PetRegisterResponse();
    }

    //수정
    @Transactional
    public PetUpdateResponse updatePetProfile(Long userId, Long petId, PetUpdateRequest req,
        MultipartFile profileImage) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));

        if (!userId.equals(pet.getUser().getId())) {
            throw new GlobalException(BAD_REQUEST);
        }

        log.info("Profile ProfileImage in Service: {}", profileImage.getOriginalFilename());
        log.info("Profile ProfileImage Content Type: {}", profileImage.getContentType());
        log.info("Profile ProfileImage Size: {}", profileImage.getSize());

        if (!profileImage.isEmpty()) {
            String newImageUrl = uploadProfileImage(profileImage);

            if (!pet.getProfileImage().isBasic()) {
                s3Service.deleteFile(pet.getProfileImageUrl());
                profileImageRepository.deleteById(pet.getProfileImage().getId());
            }

            ProfileImage image = ProfileImage.builder()
                .type(Type.PET)
                .isBasic(false)
                .imageUrl(newImageUrl)
                .build();

            ProfileImage savedImage = profileImageRepository.save(image);

            pet.update(req.getName(), req.getAge(), req.isNeutered(), savedImage);
        } else {
            pet.update(req.getName(), req.getAge(), req.isNeutered(), pet.getProfileImage());
        }

        return PetUpdateResponse.fromEntity(pet);
    }

    public String uploadProfileImage(MultipartFile profileImage) {
        return s3Service.uploadImage(profileImage, "pet-profile-images");
    }

    //삭제
    public void deletePetProfile(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
            .orElseThrow(() -> new GlobalException(NOT_FOUND_PET_PROFILE));
        if (!userId.equals(pet.getUser().getId())) {
            throw new GlobalException(BAD_REQUEST);
        }
        petRepository.deleteById(petId);
    }
}
