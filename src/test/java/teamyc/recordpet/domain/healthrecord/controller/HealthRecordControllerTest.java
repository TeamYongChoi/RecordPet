package teamyc.recordpet.domain.healthrecord.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthEvent;
import teamyc.recordpet.domain.healthrecord.entitiy.HealthRecord;
import teamyc.recordpet.domain.healthrecord.repository.HealthRecordRepository;
import teamyc.recordpet.domain.pet.entity.Gender;
import teamyc.recordpet.domain.pet.entity.Pet;
import teamyc.recordpet.domain.pet.repository.PetRepository;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class HealthRecordControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Pet testPet;

    @BeforeEach
    void MockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        petRepository.deleteAll();
        healthRecordRepository.deleteAll();
        testPet = createDefaultPet();
    }

    //등록
    @DisplayName("건강기록 등록 성공")
    @Test
    public void addHealthRecordSuccess() throws Exception {
        //given
        final String url = "/api/v1/pets/{id}/health-records";
        final String jsonRequest = """
                {
                "title": "기침 기록",
                "events": [
                        {
                            "occurrenceTime": "2025-01-01T10:30:00",
                            "content": "기침 발생"
                        },
                        {
                            "occurrenceTime": "2025-01-01T12:30:00",
                            "content": "구토 발생"
                        }
                    ]
                }
                """;

        //when
        ResultActions result = mockMvc.perform(post(url, testPet.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest));

        //then
        result.andExpect(status().isOk());
    }

    //조회(월별)
    @DisplayName("월별 조회 성공 - 여러 기록")
@Test
void findHealthRecordsByMonth() throws Exception {
    final String url = "/api/v1/pets/{id}/health-records/month";

    //1. 1월 기록 1
    HealthRecord record1 = HealthRecord.builder()
            .pet(testPet)
            .title("기침 기록")
            .build();

    HealthEvent event1 = HealthEvent.builder()
            .occurrenceTime(LocalDateTime.of(2025, 1, 1, 10, 30))
            .content("기침 발생")
            .healthRecord(record1)
            .build();

    record1.addEvent(event1);

    //2. 1월 기록 2
    HealthRecord record2 = HealthRecord.builder()
            .pet(testPet)
            .title("열 기록")
            .build();

    HealthEvent event2 = HealthEvent.builder()
            .occurrenceTime(LocalDateTime.of(2025, 1, 2, 15, 30))
            .content("발열")
            .healthRecord(record2)
            .build();

    record2.addEvent(event2);

    //3. 12월 기록 (조회되지 않아야 함)
    HealthRecord record3 = HealthRecord.builder()
            .pet(testPet)
            .title("12월 감기 기록")
            .build();

    HealthEvent event3 = HealthEvent.builder()
            .occurrenceTime(LocalDateTime.of(2024, 12, 25, 20, 0))
            .content("심한 기침")
            .healthRecord(record3)
            .build();

    record3.addEvent(event3);

    // 저장
    healthRecordRepository.save(record1);
    healthRecordRepository.save(record2);
    healthRecordRepository.save(record3);

    //1월 데이터만 가져오기
    ResultActions result = mockMvc.perform(get(url, testPet.getId())
            .param("year", "2025")
            .param("month", "1"));

    result.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.length()").value(2)) // ✅ 1월 기록 2개여야 함
            .andExpect(jsonPath("$.data[0].title").value("기침 기록"))
            .andExpect(jsonPath("$.data[1].title").value("열 기록"));
    }


    //조회(전체)
    @DisplayName("전체 건강기록 조회 성공")
@Test
void findAllHealthRecords() throws Exception {
    final String url = "/api/v1/pets/{id}/health-records/all";

    //1. 테스트 데이터 생성
    HealthRecord record1 = HealthRecord.builder()
            .pet(testPet)
            .title("기침 기록")
            .build();

    HealthEvent event1 = HealthEvent.builder()
            .occurrenceTime(LocalDateTime.of(2025, 1, 1, 10, 30))
            .content("기침 발생")
            .healthRecord(record1)
            .build();

    record1.addEvent(event1);

    HealthRecord record2 = HealthRecord.builder()
            .pet(testPet)
            .title("발열 기록")
            .build();

    HealthEvent event2 = HealthEvent.builder()
            .occurrenceTime(LocalDateTime.of(2025, 1, 2, 15, 30))
            .content("고열")
            .healthRecord(record2)
            .build();

    record2.addEvent(event2);

    HealthRecord record3 = HealthRecord.builder()
            .pet(testPet)
            .title("피부 질환 기록")
            .build();

    HealthEvent event3 = HealthEvent.builder()
            .occurrenceTime(LocalDateTime.of(2024, 12, 20, 18, 45))
            .content("피부 발진")
            .healthRecord(record3)
            .build();

    record3.addEvent(event3);

    //저장
    healthRecordRepository.save(record1);
    healthRecordRepository.save(record2);
    healthRecordRepository.save(record3);

    //when - GET 요청 (page=0, size=10)
    ResultActions result = mockMvc.perform(get(url, testPet.getId())
            .param("page", "0")
            .param("size", "10")
            .param("sort", "id,desc")); // 정렬 기준 id 내림차순

    //then - 응답 검증
    result.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.content.length()").value(3)) // ✅ 전체 3개여야 함
            .andExpect(jsonPath("$.data.content[0].title").value("피부 질환 기록")) // 가장 최근 기록
            .andExpect(jsonPath("$.data.content[1].title").value("발열 기록"))
            .andExpect(jsonPath("$.data.content[2].title").value("기침 기록"));
}


    private Pet createDefaultPet() {
        return petRepository.save(Pet.builder()
                .name("바둑쓰")
                .age(3)
                .gender(Gender.F)
                .isNeutered(false)
                .photoUrl("sampleUrl")
                .build());
    }
}