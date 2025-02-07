package teamyc.recordpet.domain.pet.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import teamyc.recordpet.RecordPetApplication;
import teamyc.recordpet.domain.pet.dto.PetRegisterRequest;
import teamyc.recordpet.domain.pet.dto.PetUpdateRequest;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;
import teamyc.recordpet.global.s3.S3Service;

@SpringBootTest(classes = RecordPetApplication.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void MockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .build();
        petRepository.deleteAll();
    }


    @DisplayName("펫 프로필 등록 성공")
    @Test
    public void addPetSuccess() throws Exception {
        // given
        final String url = "/api/v1/pets";
        final String name = "test";
        final int age = 3;
        final Gender gender = Gender.F;
        final Boolean isNeutered = false;

        // JSON 데이터 생성
        String requestBody = objectMapper.writeValueAsString(
            new PetRegisterRequest(name, age, gender, isNeutered)
        );

        // MockMultipartFile 생성
        MockMultipartFile profileImage = new MockMultipartFile(
            "profileImage",              // @RequestPart 이름
            "test.jpg",                  // 파일 이름
            MediaType.IMAGE_JPEG_VALUE,  // MIME 타입
            "image-content".getBytes()   // 파일 내용
        );

        MockMultipartFile jsonRequest = new MockMultipartFile(
            "req",                       // @RequestBody 이름
            "",
            MediaType.APPLICATION_JSON_VALUE,
            requestBody.getBytes()       // JSON 직렬화된 데이터
        );

        // when
        ResultActions result = mockMvc.perform(multipart(url)
            .file(profileImage)              // 파일 데이터
            .file(jsonRequest)               // JSON 데이터
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE) // Content-Type 설정
            .characterEncoding("UTF-8"));

        // then
        result
            .andExpect(status().isOk()) // 201 상태 코드 확인
            .andExpect(jsonPath("$.httpStatus").value("CREATED"));

        // 데이터베이스 확인
        List<Pet> pets = petRepository.findAll();

        assertThat(pets.size()).isEqualTo(1);
        assertThat(pets.get(0).getName()).isEqualTo(name);
        assertThat(pets.get(0).getAge()).isEqualTo(age);
        assertThat(pets.get(0).getGender()).isEqualTo(gender);
        assertThat(pets.get(0).getIsNeutered()).isEqualTo(isNeutered);
        assertThat(pets.get(0).getProfileImageUrl()).isNotNull(); // 업로드된 URL 확인
    }

    @DisplayName("펫 프로필 수정 성공")
    @Test
    public void updatePetSuccess() throws Exception {
        // given
        final String url = "/api/v1/pets/{id}";

        Pet savedPet = createDefaultPet();
        assertThat(savedPet).isNotNull();
        assertThat(savedPet.getId()).isNotNull();

        final String newName = "UpdatedName";
        final int newAge = 5;
        final Boolean newIsNeutered = true;

        File testFile = new File(
            System.getProperty("user.dir") + "/src/test/resources/test-image.jpg");
        MockMultipartFile profileImage = new MockMultipartFile(
            "profileImage",                        // @RequestPart 이름
            "test-image.jpg",                      // 파일 이름
            MediaType.IMAGE_JPEG_VALUE,            // MIME 타입
            new FileInputStream(testFile)          // 실제 파일 데이터
        );

        MockMultipartFile requestBody = new MockMultipartFile(
            "req",                       // @RequestPart("req") 이름과 일치해야 함
            "",
            MediaType.APPLICATION_JSON_VALUE,
            objectMapper.writeValueAsString(
                PetUpdateRequest.builder()
                    .name(newName)
                    .age(newAge)
                    .isNeutered(newIsNeutered)
                    .build()
            ).getBytes()
        );

        System.out.println("Profile ProfileImage Name: " + profileImage.getOriginalFilename());

        // when
        ResultActions result = mockMvc.perform(multipart(url, savedPet.getId())
            .file(profileImage)
            .file(requestBody)
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .with(request -> {
                request.setMethod("PUT");
                return request;
            }));

        // then
        result.andExpect(status().isOk());

        // 데이터 검증
        Pet updatedPet = petRepository.findById(savedPet.getId()).orElseThrow();

        assertThat(updatedPet.getName()).isEqualTo(newName);
        assertThat(updatedPet.getAge()).isEqualTo(newAge);
        assertThat(updatedPet.getIsNeutered()).isEqualTo(newIsNeutered);
        assertThat(updatedPet.getProfileImageUrl()).isNotNull();
    }

    @DisplayName("펫 프로필 목록 조회 성공")
    @Test
    public void findAllPetsSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets";
        Pet savedPet = createDefaultPet();
        //when
        final ResultActions result = mockMvc.perform(
            get(url).accept(MediaType.APPLICATION_JSON_VALUE));
        //then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.[0].name").value(savedPet.getName()))
            .andExpect(jsonPath("$.data.[0].age").value(savedPet.getAge()))
            .andExpect(jsonPath("$.data.[0].gender").value(savedPet.getGender().toString()))
            .andExpect(jsonPath("$.data.[0].isNeutered").value(savedPet.getIsNeutered().toString()))
            .andExpect(jsonPath("$.data.[0].photoUrl").value(savedPet.getProfileImageUrl()));
    }

    @DisplayName("펫 프로필 단건 조회 성공")
    @Test
    public void findPetSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets/{id}";
        Pet savedPet = createDefaultPet();
        //when
        final ResultActions result = mockMvc.perform(get(url, savedPet.getId()));
        //then

        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.name").value(savedPet.getName()))
            .andExpect(jsonPath("$.data.age").value(savedPet.getAge()))
            .andExpect(jsonPath("$.data.gender").value(savedPet.getGender().toString()))
            .andExpect(jsonPath("$.data.isNeutered").value(savedPet.getIsNeutered()))
            .andExpect(jsonPath("$.data.photoUrl").value(savedPet.getProfileImageUrl()));
    }

    @DisplayName("펫 프로필 삭제 성공")
    @Test
    public void deletePetSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets/{id}";
        Pet savedPet = createDefaultPet();

        //when
        mockMvc.perform(delete(url, savedPet.getId()))
            .andExpect(status().isOk());
        //then

        List<Pet> pets = petRepository.findAll();
        assertThat(pets).isEmpty();
    }

    private Pet createDefaultPet() {
        return petRepository.save(Pet.builder()
            .name("petName")
            .age(3)
            .gender(Gender.F)
            .isNeutered(false)
//            .profileImage("sampleUrl")
            .build());
    }
}