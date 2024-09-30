package com.fivemybab.ittabab.inquiry.command.application.controller;

import com.fivemybab.ittabab.inquiry.command.application.dto.InquiryAnswerResponse;
import com.fivemybab.ittabab.inquiry.command.application.dto.InquiryQuestionResponse;
import com.fivemybab.ittabab.inquiry.command.application.service.InquiryCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
@Tag(name = "Inquiry", description = "문의 관련 API")
public class InquiryCommandController {

    private final InquiryCommandService inquiryCommandService;

//    @GetMapping("/list")
//    public ResponseEntity<List<InquiryDto>> findInquiryList() {
//        List<InquiryDto> inquiryList = inquiryService.findInquiryList();
//        return new ResponseEntity<>(inquiryList, HttpStatus.OK);
//    }
//
//    /* 문의 조회 (사용자)*/
//    @GetMapping("/list/user/{userId}")
//    public ResponseEntity<List<InquiryDto>> findInquiryListByUserId(@PathVariable Long userId) {
//        List<InquiryDto> inquiryId = inquiryService.findInquiryListByUserId(userId);
//        return new ResponseEntity<>(inquiryId,HttpStatus.OK);
//    }

    /* 문의 등록 (사용자) */
    @Operation(summary = "문의 등록(사용자)")
    @PostMapping
    public ResponseEntity<String> registInquiryQuestion(@RequestBody InquiryQuestionResponse inquiryQuestionResponse) {
        inquiryQuestionResponse.setCreateDate(LocalDateTime.now());
        inquiryCommandService.registInquiryQuestion(inquiryQuestionResponse);
        return new ResponseEntity<>("문의 등록(사용자) 완료", HttpStatus.CREATED);
    }


    /* 문의 답변 (관리자) */
    @Operation(summary = "문의 답변(관리자)")
    @PostMapping("/{inquiryId}")
    public ResponseEntity<String> registInquiryAnswer( @PathVariable Long inquiryId,
                                                       @RequestBody InquiryAnswerResponse inquiryanswerResponse) {
        if (inquiryanswerResponse.getResponseUserId() == null || inquiryanswerResponse.getInquiryReply() == null) {
            return new ResponseEntity<>("responseUserId 또는 inquiryReply가 누락되었습니다.", HttpStatus.BAD_REQUEST);
        }

        // 서비스 호출로 답변을 등록
        inquiryCommandService.registInquiryAnswer(inquiryId, inquiryanswerResponse.getInquiryReply(), inquiryanswerResponse.getResponseUserId());

        return new ResponseEntity<>("문의 답변 등록(관리자) 완료", HttpStatus.CREATED);
    }
}
