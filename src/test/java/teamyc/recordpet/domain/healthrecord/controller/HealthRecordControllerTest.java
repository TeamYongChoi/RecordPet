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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @DisplayName("일별 조회 성공 - 특정 날짜의 기록 조회")
    @Test
    void findHealthRecordsByDate() throws Exception {
        final String url = "/api/v1/pets/{id}/health-records/day";

        // 1. 2025년 1월 5일 기록 1
        HealthRecord record1 = HealthRecord.builder()
                .pet(testPet)
                .title("기침 기록")
                .build();

        HealthEvent event1 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 5, 10, 30))
                .content("기침 발생")
                .healthRecord(record1)
                .build();

        HealthEvent event2 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 5, 14, 00))
                .content("기침 악화")
                .healthRecord(record1)
                .build();

        record1.addEvent(event1);
        record1.addEvent(event2);

        // 2. 2025년 1월 5일 기록 2
        HealthRecord record2 = HealthRecord.builder()
                .pet(testPet)
                .title("발열 기록")
                .build();

        HealthEvent event3 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 5, 15, 30))
                .content("고열")
                .healthRecord(record2)
                .build();

        record2.addEvent(event3);

        // 3. 2025년 1월 4일 기록 (조회되지 않아야 함)
        HealthRecord record3 = HealthRecord.builder()
                .pet(testPet)
                .title("두통 기록")
                .build();

        HealthEvent event4 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 4, 12, 0))
                .content("심한 두통")
                .healthRecord(record3)
                .build();

        record3.addEvent(event4);

        // 저장
        healthRecordRepository.save(record1);
        healthRecordRepository.save(record2);
        healthRecordRepository.save(record3);

        // 2025년 1월 5일 데이터만 가져오기
        ResultActions result = mockMvc.perform(get(url, testPet.getId())
                .param("date", "2025-01-05"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].title").value("기침 기록"))
                .andExpect(jsonPath("$.data[0].events.length()").value(2))
                .andExpect(jsonPath("$.data[1].title").value("발열 기록"))
                .andExpect(jsonPath("$.data[1].events.length()").value(1));
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
                .andExpect(jsonPath("$.data.length()").value(2))
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
                .andExpect(jsonPath("$.data.content.length()").value(3))
                .andExpect(jsonPath("$.data.content[0].title").value("피부 질환 기록")) // 가장 최근 기록
                .andExpect(jsonPath("$.data.content[1].title").value("발열 기록"))
                .andExpect(jsonPath("$.data.content[2].title").value("기침 기록"));
    }

    @Test
    @DisplayName("건강 기록 수정 성공")
    void updateHealthRecordSuccess() throws Exception {
        final String url = "/api/v1/pets/{petId}/health-records/{recordId}";

        // 테스트용 건강 기록 생성
        HealthRecord testRecord = HealthRecord.builder()
                .pet(testPet)
                .title("기침 기록")
                .build();

        // 테스트용 이벤트 2개 생성
        HealthEvent testEvent1 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 1, 10, 30))
                .content("기침 발생")
                .healthRecord(testRecord)
                .build();

        HealthEvent testEvent2 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 1, 12, 30))
                .content("구토 발생")
                .healthRecord(testRecord)
                .build();

        testRecord.addEvent(testEvent1);
        testRecord.addEvent(testEvent2);

        healthRecordRepository.save(testRecord);

        // 수정할 데이터 (제목 변경 & 이벤트 추가)
        String updateRequest = """
                {
                    "title": "수정된 기침 기록",
                    "events": [
                        {
                            "occurrenceTime": "2025-01-02T08:00:00",
                            "content": "미열 발생"
                        }
                    ]
                }
                """;

        mockMvc.perform(put(url, testPet.getId(), testRecord.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk());

        HealthRecord healthRecord = healthRecordRepository.findByIdAndPetId(testRecord.getId(), testPet.getId()).orElseThrow();

        assertThat(healthRecord.getTitle()).isEqualTo("수정된 기침 기록");
        assertThat(healthRecord.getEvents().size()).isEqualTo(1);
        assertThat(healthRecord.getEvents().get(0).getContent()).isEqualTo("미열 발생");
    }

    // ✅ 건강 기록 삭제 테스트
    @Test
    @DisplayName("건강 기록 삭제 성공")
    void deleteHealthRecordSuccess() throws Exception {

        // 테스트용 건강 기록 생성
        HealthRecord testRecord = HealthRecord.builder()
                .pet(testPet)
                .title("기침 기록")
                .build();

        // 테스트용 이벤트 2개 생성
        HealthEvent testEvent1 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 1, 10, 30))
                .content("기침 발생")
                .healthRecord(testRecord)
                .build();

        HealthEvent testEvent2 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 1, 12, 30))
                .content("구토 발생")
                .healthRecord(testRecord)
                .build();

        testRecord.addEvent(testEvent1);
        testRecord.addEvent(testEvent2);

        healthRecordRepository.save(testRecord);

        final String url = "/api/v1/pets/{petId}/health-records/{recordId}";

        mockMvc.perform(delete(url, testPet.getId(), testRecord.getId()))
                .andExpect(status().isOk());

        assertFalse(healthRecordRepository.existsById(testRecord.getId()));
    }

    @Test
    @DisplayName("건강 이벤트 삭제 성공")
    void deleteHealthEventSuccess() throws Exception {
        final String url = "/api/v1/pets/{petId}/health-records/{recordId}/events/{eventId}";

        // 테스트용 건강 기록 생성
        HealthRecord testRecord = HealthRecord.builder()
                .pet(testPet)
                .title("기침 기록")
                .build();

        // 테스트용 이벤트 2개 생성
        HealthEvent testEvent1 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 1, 10, 30))
                .content("기침 발생")
                .healthRecord(testRecord)
                .build();

        HealthEvent testEvent2 = HealthEvent.builder()
                .occurrenceTime(LocalDateTime.of(2025, 1, 1, 12, 30))
                .content("구토 발생")
                .healthRecord(testRecord)
                .build();

        testRecord.addEvent(testEvent1);
        testRecord.addEvent(testEvent2);

        healthRecordRepository.save(testRecord);

        mockMvc.perform(delete(url, testPet.getId(), testRecord.getId(), testEvent1.getId()))
                .andExpect(status().isOk());

        HealthRecord updatedRecord = healthRecordRepository.findByIdAndPetId(testRecord.getId(), testPet.getId())
                .orElseThrow(() -> new RuntimeException("HealthRecord not found for pet with id: " + testPet.getId()));
        assertEquals(1, updatedRecord.getEvents().size()); // 이벤트가 1개만 남았는지 확인
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