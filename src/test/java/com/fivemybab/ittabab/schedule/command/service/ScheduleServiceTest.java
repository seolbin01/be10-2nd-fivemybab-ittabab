package com.fivemybab.ittabab.schedule.command.service;

import com.fivemybab.ittabab.schedule.command.application.dto.ScheduleCreateRequire;
import com.fivemybab.ittabab.schedule.command.application.dto.ScheduleDto;
import com.fivemybab.ittabab.schedule.command.application.service.ScheduleCommandService;
import com.fivemybab.ittabab.schedule.command.domain.aggregate.ScheduleInfo;
import com.fivemybab.ittabab.schedule.command.domain.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private ScheduleCommandService scheduleCommandService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    private ScheduleCreateRequire testScheduleDto;

    @BeforeEach
    void setUp() {
        testScheduleDto = new ScheduleCreateRequire();
        testScheduleDto.setScheduleTitle("일정 제목 입니다.");
        testScheduleDto.setScheduleContent("일정 내용 입니다.");
        testScheduleDto.setScheduleDate(LocalDate.now());
    }

    @Test
    @DisplayName("일정 등록 테스트")
    void registScheduleTest() {
        // 일정 등록
        scheduleCommandService.registSchedule(testScheduleDto,1L);

        // 일정이 잘 등록되었는지 확인
        ScheduleInfo savedSchedule = scheduleRepository.findAll().get(2);
        // findAll은 List로 반환, 3번째 요소를 확인
        assertNotNull(savedSchedule);
        assertEquals("일정 제목 입니다.", savedSchedule.getScheduleTitle());
        assertEquals("일정 내용 입니다.", savedSchedule.getScheduleContent());
        assertEquals(LocalDate.now(), savedSchedule.getScheduleDate());
    }

    @Test
    @DisplayName("일정 수정 테스트")
    void modifyScheduleTest() {

        // 등록된 일정 불러오기
        ScheduleInfo savedSchedule = scheduleRepository.findAll().get(0);

        // 수정할 내용 설정
        ScheduleDto updateDto = new ScheduleDto();
        updateDto.setScheduleId(savedSchedule.getScheduleId());
        updateDto.setScheduleTitle("수정된 일정 제목 입니다.");
        updateDto.setScheduleContent("수정된 일정 내용 입니다.");
        updateDto.setScheduleDate(LocalDate.now().plusDays(1));

        // 일정 수정
        scheduleCommandService.modifySchedule(updateDto);

        // 수정된 일정 확인
        ScheduleInfo updatedSchedule = scheduleRepository.findById(savedSchedule.getScheduleId()).orElseThrow();
        assertEquals("수정된 일정 제목 입니다.", updatedSchedule.getScheduleTitle());
        assertEquals("수정된 일정 내용 입니다.", updatedSchedule.getScheduleContent());
        assertEquals(LocalDate.now().plusDays(1), updatedSchedule.getScheduleDate());
    }

    @Test
    @DisplayName("일정 삭제 테스트")
    void deleteScheduleTest() {

        // 등록된 일정 불러오기
        ScheduleInfo savedSchedule = scheduleRepository.findAll().get(0);
        Long scheduleId = savedSchedule.getScheduleId();

        // 일정 삭제
        scheduleCommandService.deleteSchedule(scheduleId);

        // 삭제된 일정이 존재하지 않는지 확인
        assertFalse(scheduleRepository.existsById(scheduleId));
    }

}